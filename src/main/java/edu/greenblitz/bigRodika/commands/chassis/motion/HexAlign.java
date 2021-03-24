package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.GBSubsystem;
import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.gears.Gear;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.profiling.ProfilingConfiguration;

import java.util.ArrayList;
import java.util.List;

public class HexAlign extends GBCommand {

    private Follow2DProfileCommand prof;
    private ThreadedCommand cmd;
    private ProfilingConfiguration config;
    private double k;
    private double r;
    private int profileAngleVsGyroInverted = -1;
    private int gyroInverted = 1;
    private Point globHexPos;
    private boolean fucked = false;
    private double driveTolerance;
    private double tolerance;
    private List<Double> radsAndCritPoints;//crit point - radius - crit - radius - crit .... - radius
    private Position globalEnd;
    private double maxPower;


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
        this.tolerance = tolerance;
        this.driveTolerance = driveTolerance;
        this.maxPower = maxPower;
        this.config = config;
    }

    public HexAlign(double r, double k, double driveTolerance, double tolerance, double maxPower) {
        this(r, k, driveTolerance, tolerance, maxPower, RobotMap.Limbo2.Chassis.MotionData.CONFIG);
    }

    public HexAlign(List<Double> radsAndCritPoints, double k, double driveTolerance, double tolerance, double maxPower) {
        this(radsAndCritPoints, k, driveTolerance, tolerance, maxPower, RobotMap.Limbo2.Chassis.MotionData.CONFIG);
    }

    public Point getHexPos() {
        return globHexPos;
    }

    private Gear gearBefore;

    @Override
    public void initialize() {

        gearBefore = Shifter.getInstance().getCurrentGear();
        Shifter.getInstance().setShift(Gear.POWER);

        double absAng = gyroInverted * (Chassis.getInstance().getAngle());// + RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET);

        State startState = new State(0, 0, profileAngleVsGyroInverted * absAng);
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        VisionLocation location = VisionMaster.getInstance().getVisionLocation();
//        SmartDashboard.putString("Vision Location", location.toString());
        double[] difference = MotionUtils.getSimulatedVisionLocation();

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

        Double loc = RobotMap.Limbo2.Dome.DOME.get(r);
        if (loc != null) {
            new DomeApproachSwiftly(loc).schedule();
        } else {
            System.out.println("Cant find " + r + " in dome radii");
        }

//        SmartDashboard.putNumber("rds", r);

        double desRadCenter = r + RobotMap.Limbo2.Chassis.VISION_CAM_Y_DIST_CENTER;
        double errRadCenter = Math.abs(radCenter - desRadCenter);

//        SmartDashboard.putNumber("errRadCenter", errRadCenter);

        //if robot is very very  - do nothing
        if (errRadCenter < tolerance) {
            fucked = true;
            return;
        }

//        SmartDashboard.putBoolean("inDriveTol", errRadCenter < driveTolerance);

        //if robot is close - drives straight
        if (errRadCenter < driveTolerance) {
            k = 1;
        }

        //assume targetY != 0
        double relAng = Math.atan(targetX / targetY);

        Point hexPos = new Point(targetX, targetY).rotate(-absAng);

        globHexPos = new Point(hexPos.getX() + Chassis.getInstance().getLocation().getX(),
                hexPos.getY() + Chassis.getInstance().getLocation().getY());

//        SmartDashboard.putString("hex", hexPos.toString());

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
//            SmartDashboard.putBoolean("As expected", true);
        }

//        SmartDashboard.putNumber("Hex Anlign angle", angle);

        //calculates the end point
        State endState = new State(
                hexPos.getX() + r * Math.cos(angle),
                hexPos.getY() - r * Math.sin(angle),
                profileAngleVsGyroInverted * (Math.PI / 2 - angle));

        //if robot is close - drives straight
        if (errRadCenter < driveTolerance) {
            endState.setAngle(startState.getAngle());
        } else {
            //translates the profile from mid front to mid mid
            endState.translate(new Point(0, cam_y).rotate(-absAng)).translate(new Point(0, -cam_y).rotate(endState.getAngle()));
        }

//        Point rotatePos = endState.clone().rotate(-startState.getAngle());
//        double ang;
//        if (rotatePos.getY() == 0) {
//            ang = Math.PI;
//        } else if (rotatePos.getX() == 0) {
//            ang = 0;
//        } else {
//            ang = (Math.PI / 2) - Math.atan2(rotatePos.getY() * rotatePos.getY()
//                    - rotatePos.getX() * rotatePos.getX(), 2 * rotatePos.getX() * rotatePos.getY());
//        }
//        startState.setAngle(0);
//        endState.setAngle(ang);

        if (errRadCenter < driveTolerance){
            startState.rotate(-startState.getAngle());
            endState.rotate(-startState.getAngle());
            endState.setX(0);
            startState.setAngle(0);
            endState.setAngle(0);
        }

        //creates path
        List<State> path = new ArrayList<>();
        path.add(startState);
        path.add(endState);

        //determines if reversed
        boolean reverse = Math.sqrt(Math.pow(difference[0], 2) + Math.pow(difference[1], 2)) < r;

//        SmartDashboard.putString("start", startState.toString());
//        SmartDashboard.putString("end", endState.toString());
//        System.err.println("end" + endState.toString());


        globalEnd = new Position(Chassis.getInstance().getLocation().getX() + endState.getX(),
                Chassis.getInstance().getLocation().getY() + endState.getY(), -endState.getAngle());

        prof = new Follow2DProfileCommand(path, config, maxPower, reverse);
        prof.setSendData(false);
        cmd = new ThreadedCommand(prof, Chassis.getInstance());
        cmd.schedule();
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interupted) {
        if (!fucked) {
            cmd.stop();
            /*SmartDashboard.putString("HexAlign error",
                    new Position(Point.subtract(Chassis.getInstance().getLocation(), globalEnd), Chassis.getInstance().getAngle() - globalEnd.getAngle()).toString());
            Shifter.getInstance().setShift(gearBefore);
        }*/
        cmd = null;
        prof = null;
    }

    @Override
    public boolean isFinished() {
        return fucked || cmd.isFinished();
    }
}