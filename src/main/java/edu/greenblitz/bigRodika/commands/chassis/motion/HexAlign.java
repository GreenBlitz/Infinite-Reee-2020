package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.profiling.ProfilingConfiguration;

import java.util.ArrayList;
import java.util.List;

public class HexAlign extends ChassisCommand {

    private Follow2DProfileCommand prof;
    private ThreadedCommand cmd;
    private ProfilingConfiguration config;
    private double k = 0.2;
    private double r = 4.2; //radius
    private int profileAngleVsGyroInverted = -1;
    private int gyroInverted = 1;
    private Point globHexPos;
    private boolean fucked = false;
    private double driveTolerance = 0.3;
    private double tolerance = 0.05;
    private List<Double> radsAndCritPoints;//crit point - radius - crit - radius - crit .... - radius
    private Position globalEnd;
    private double maxPower = 0.5;


    public HexAlign(double r, double k, double driveTolerance, double tolerance, double maxPower, ProfilingConfiguration config) {
        super();
        this.k = k;
        this.r = r;
        this.driveTolerance = driveTolerance;
        this.tolerance = tolerance;
        this.maxPower = maxPower;
        this.config = config;
    }

    public HexAlign(List<Double> radsAndCritPoints, double k, double driveTolerance, double tolerance, double maxPower, ProfilingConfiguration config) {
        super();
        this.radsAndCritPoints = radsAndCritPoints;
        this.k = k;
        this.driveTolerance = driveTolerance;
        this.maxPower = maxPower;
        this.config = config;
    }

    public HexAlign(double r, double k, double driveTolerance, double tolerance, double maxPower) {
        this(r,k,driveTolerance,tolerance,maxPower, RobotMap.Limbo2.Chassis.MotionData.CONFIG);
    }

    public HexAlign(List<Double> radsAndCritPoints, double k, double driveTolerance, double tolerance, double maxPower) {
        this(radsAndCritPoints,k,driveTolerance,tolerance,maxPower, RobotMap.Limbo2.Chassis.MotionData.CONFIG);
    }

    public HexAlign() {
        super();
    }

    public Point getHexPos() {
        return globHexPos;
    }

    @Override
    public void initialize() {
        double absAng = gyroInverted * (Chassis.getInstance().getAngle());// + RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET);

        State startState = new State(0, 0, profileAngleVsGyroInverted * absAng);
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        VisionLocation location = VisionMaster.getInstance().getVisionLocation();
        SmartDashboard.putString("Vision Location", location.toString());
        double[] difference = location.toDoubleArray();

        if (!VisionMaster.getInstance().isLastDataValid()) {
            fucked = true;
            return;
        }

        double targetX = difference[0] + RobotMap.Limbo2.Chassis.VISION_CAM_X_DIST_CENTER;
        double targetY = difference[1];

        double cam_y = RobotMap.Limbo2.Chassis.VISION_CAM_Y_DIST_CENTER;

        double radCenter = new Point(targetX,
                targetY + cam_y).norm();


        //find best radius
        if (radsAndCritPoints != null) {
            if (radCenter < radsAndCritPoints.get(0) + cam_y) {
                fucked = true;
                return;
            }
            r = radsAndCritPoints.get(radsAndCritPoints.size() - 1);
            for (int i = 0; i < radsAndCritPoints.size() - 1; i++) {
                if (radCenter < radsAndCritPoints.get(i + 1) + cam_y && radCenter >= radsAndCritPoints.get(i) + cam_y) {
                    r = radsAndCritPoints.get((i + 1) - i % 2);
                    break;
                }
            }
        }

        SmartDashboard.putNumber("rds", r);

        double desRadCenter = r + RobotMap.Limbo2.Chassis.VISION_CAM_Y_DIST_CENTER;
        double errRadCenter = Math.abs(radCenter - desRadCenter);

        SmartDashboard.putNumber("errRadCenter", errRadCenter);

        //if robot is very very close - do nothing
        if (errRadCenter < tolerance) {
            fucked = true;
            return;
        }

        SmartDashboard.putBoolean("inDriveTol", errRadCenter < driveTolerance);

        //if robot is close - drives straight
        if (errRadCenter < driveTolerance) {
            k = 1;
        }

        //assume targetY != 0
        double relAng = Math.atan(targetX / targetY);

        Point hexPos = new Point(targetX, targetY).rotate(-absAng);

        globHexPos = new Point(hexPos.getX() + Chassis.getInstance().getLocation().getX(),
                hexPos.getY() + Chassis.getInstance().getLocation().getY());

        SmartDashboard.putString("hex", hexPos.toString());

        //calculates the best point to get to, deals with exceptional cases
        double devConst = 1.5;
        double angle;
        if (Math.abs(targetX) > r) {
            if (targetX < 0)
                angle = (1 - k / devConst) * (Math.PI / 2 - absAng + relAng);
            else
                angle = (1 + k / devConst) * (Math.PI / 2 - absAng + relAng) - Math.PI * k / devConst;
        } else {
            angle = Math.PI / 2
                    - absAng
                    + relAng
                    - k * Math.asin(
                    Math.sin(-relAng) *
                            ((targetY - Math.sqrt(r * r - targetX * targetX)) / r)
            );
        }

        //calculates the end point
        State endState = new State(
                hexPos.getX() + r * Math.cos(angle),
                hexPos.getY() - r * Math.sin(angle),
                profileAngleVsGyroInverted * (Math.PI / 2 - angle));

        //translates the profile from mid front to mid mid
        endState.translate(new Point(0, cam_y).rotate(-absAng)).translate(new Point(0, -cam_y).rotate(endState.getAngle()));

        //if robot is close - drives straight
        if (errRadCenter < driveTolerance) {
            endState.setAngle(startState.getAngle());
        }

        //creates path
        List<State> path = new ArrayList<>();
        path.add(startState);
        path.add(endState);

        SmartDashboard.putString("end", endState.toString());
        System.err.println("end" + endState.toString());

        //determines if reversed
        boolean reverse = Math.sqrt(Math.pow(difference[0], 2) + Math.pow(difference[1], 2)) < r;

        SmartDashboard.putString("start", startState.toString());
        SmartDashboard.putString("end1", endState.toString());
        System.err.println("end1" + endState.toString());

        //globalEnd = new Position(Chassis.getInstance().getLocation()).translate(endState);

        prof = new Follow2DProfileCommand(path, config, maxPower, reverse);
        cmd = new ThreadedCommand(prof);
        cmd.initialize();
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interupted) {
        if (!fucked) cmd.end(interupted);
        //SmartDashboard.putString("HexAlign error",
        //Point.subtract(Chassis.getInstance().getLocation(), globalEnd).toString());
    }

    @Override
    public boolean isFinished() {
        if (fucked) return true;
        return cmd.isFinished();
    }
}
