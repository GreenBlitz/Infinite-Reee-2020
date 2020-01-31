package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class HexAlign extends ChassisCommand {

    private Follow2DProfileCommand prof;
    private ThreadedCommand cmd;
    private double k = 0.2;
    private double r = 3.7; //radius
    private Point globHexPos;
    private boolean fucked = false;
    private double driveTolerance = 0.3;
    private double tolerance = 0.05;
    private List<Double> radsAndCritPoints;//crit point - radius - crit - radius - crit .... - radius
    private double endAng;

    public HexAlign(double r, double k) {
        this.k = k;
        this.r = r;
    }

    public HexAlign(List<Double> radsAndCritPoints, double k, double driveTolerance) {
        this.radsAndCritPoints = radsAndCritPoints;
        this.k = k;
        this.driveTolerance = driveTolerance;
    }

    public HexAlign() {
        super();
    }

    public Point getHexPos() {
        return globHexPos;
    }

    @Override
    public void initialize() {
        State startState = new State(0, 0, -Chassis.getInstance().getAngle());
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();

        double[] difference = VisionMaster.getInstance().getVisionLocation().toDoubleArray();

        if (!VisionMaster.getInstance().isLastDataValid()) {
            fucked = true;
            return;
        }

        double targetX = difference[0] + RobotMap.BigRodika.Chassis.VISION_CAM_X_DIST_CENTER;
        double targetY = difference[1];

        double cam_y =  RobotMap.BigRodika.Chassis.VISION_CAM_Y_DIST_CENTER;

        double radCenter = new Point(targetX,
                targetY + cam_y).norm();

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

        double desRadCenter = r + RobotMap.BigRodika.Chassis.VISION_CAM_Y_DIST_CENTER;
        //TODO This is inaccurate, if cam is not in the middle of the X dir of the robot we are screwed
        double errRadCenter = Math.abs(radCenter - desRadCenter);

        SmartDashboard.putNumber("errRadCenter", errRadCenter);
        //can be done without all of this definitions, just so the code would be readable

        if (errRadCenter < tolerance) {
            fucked = true;
            return;
        }

        SmartDashboard.putBoolean("inDriveTol", errRadCenter < driveTolerance);

        if (errRadCenter < driveTolerance) {
            k = 1;
        }

        //assume targetY != 0
        double relAng = Math.atan(targetX / targetY);
        double absAng = Chassis.getInstance().getAngle();

        Point hexPos = new Point(targetX, targetY).rotate(-absAng);

        globHexPos = new Point(hexPos.getX() + Chassis.getInstance().getLocation().getX(),
                hexPos.getY() + Chassis.getInstance().getLocation().getY());
        SmartDashboard.putString("hex", hexPos.toString());
        System.err.println("hex " + hexPos.toString());

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

        State endState = new State(hexPos.getX() + r * Math.cos(angle),
                hexPos.getY() - r * Math.sin(angle),
                -(Math.PI / 2 - angle));

        endState.translate(new Point(0, cam_y).rotate(-absAng)).translate(new Point (0,-cam_y).rotate(endState.getAngle()));

        endAng = -endState.getAngle();

         if(errRadCenter < driveTolerance){
         endState.setAngle(startState.getAngle());
         }

        List<State> path = new ArrayList<>();
        path.add(startState);
        path.add(endState);

        SmartDashboard.putString("end", endState.toString());
        System.err.println("end" + endState.toString());

        ProfilingData data = RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5");

        boolean reverse  =   Math.sqrt(Math.pow(difference[0], 2) + Math.pow(difference[1], 2)) < r;

        if(reverse){
            endState.setAngle(endState.getAngle() + Math.PI);
            startState.setAngle(startState.getAngle() + Math.PI);
        }


        prof = new Follow2DProfileCommand(path,
                .001, 800,
                data,
                0.7, 1, 1,
                new PIDObject(0.8 / data.getMaxLinearVelocity(), 0, 6 / data.getMaxLinearAccel()), .01 * data.getMaxLinearVelocity(),
                new PIDObject(0.5 / data.getMaxAngularVelocity(), 0, 0 / data.getMaxAngularAccel()), .01 * data.getMaxAngularVelocity(),
                reverse);
        cmd = new ThreadedCommand(prof);
        cmd.initialize();
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interupted) {
        if (!fucked) cmd.end(interupted);
    }

    @Override
    public boolean isFinished() {
        if (fucked) return true;
        return cmd.isFinished();
    }
}
