package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.profiling.ActuatorLocation;
import org.greenblitz.motion.profiling.MotionProfile1D;
import org.greenblitz.motion.profiling.Profiler1D;

public class TurnToAngle extends GBCommand {

    private ActuatorLocation end;
    private MotionProfile1D motionProfile;
    private double locP, velP;
    private long t0;
    private double maxV, maxA;
    private double power;

    private boolean over;

    public TurnToAngle(double angleToTurnDeg, double locP, double velP,
                       double maxV, double maxA,
                       double power) {
        super(Chassis.getInstance());
        this.locP = locP;
        this.velP = velP;
        this.maxA = maxA;
        this.maxV = maxV;
        this.power = power;
        this.end = new ActuatorLocation(Math.toRadians(angleToTurnDeg), 0);
    }

    @Override
    public void initialize() {
        ActuatorLocation start = new ActuatorLocation(Chassis.getInstance().getAngle(),
                0);

        this.motionProfile = Profiler1D.generateProfile(
                maxV,
                maxA,
                -maxA,
                0, start, end);

        t0 = System.currentTimeMillis();
        over = false;
    }

    @Override
    public void execute() {

        double timePassed = (System.currentTimeMillis() - t0) / 1000.0;

        if (motionProfile.isOver(timePassed)){
            Chassis.getInstance().moveMotors(0,0);
            over = true;
            return;
        }

        double velocity = motionProfile.getVelocity(timePassed);
        double perWheelVel = velocity * Chassis.getInstance().getWheelDistance() / 2.0;
        double accel = motionProfile.getAcceleration(timePassed);
        double location = motionProfile.getLocation(timePassed);

        double ff = velocity/maxV  + accel/maxA;
        double locPVal = locP * Position.normalizeAngle(location - Chassis.getInstance().getAngle());
        double velPLeft = velP * (perWheelVel - Chassis.getInstance().getLeftRate());
        double velPRight = velP * (-perWheelVel - Chassis.getInstance().getLeftRate());

        Chassis.getInstance().moveMotors(
                clamp(ff + locPVal + velPLeft),
                -clamp(ff + locPVal + velPRight));
    }

    private double clamp(double inp){
        return Math.copySign(Math.min(Math.abs(inp), 1.0), inp) * power;
    }

    @Override
    public void end(boolean interrupted) {
        double err = Math.toDegrees(Chassis.getInstance().getAngle() - end.getX());
        SmartDashboard.putNumber("Final Error", err);
        if (Math.abs(err) > 2 && !interrupted){
            new TurnToAngle(Math.toDegrees(end.getX()), locP, velP, maxV, maxA, power).schedule();
        }
    }

    @Override
    public boolean isFinished() {
        return over;
    }
}
