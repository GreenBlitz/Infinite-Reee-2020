package edu.greenblitz.bigRodika.commands.chassis.turns;

import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.profiling.ActuatorLocation;
import org.greenblitz.motion.profiling.MotionProfile1D;
import org.greenblitz.motion.profiling.Profiler1D;

public class TurnToAngle extends ChassisCommand {

    private static int ERROR_NORMALIZE;

    private ActuatorLocation end;
    private MotionProfile1D motionProfile;
    private double power, locP, velP, maxV, maxA;
    private long t0;

    private int overCount;

    public TurnToAngle(double angleToTurnDeg, double locP, double velP,
                       double maxV, double maxA,
                       double power) {
        this.locP = locP;
        this.velP = velP;
        this.maxA = maxA;
        this.maxV = maxV;
        this.power = power;
        this.end = new ActuatorLocation(Math.toRadians(angleToTurnDeg), 0);
    }

    @Override
    public void initialize() {
        ActuatorLocation start = new ActuatorLocation(chassis.getAngle(),
                0);

        this.motionProfile = Profiler1D.generateProfile(
                maxV,
                maxA,
                -maxA,
                0, start, end);

        t0 = System.currentTimeMillis();
        overCount = 0;
    }

    @Override
    public void execute() {

        double timePassed = (System.currentTimeMillis() - t0) / 1000.0;

        if (motionProfile.isOver(timePassed)) {
            chassis.moveMotors(0, 0);
            overCount++;
            return;
        }

        double velocity = motionProfile.getVelocity(timePassed);
        double perWheelVel = velocity * chassis.getWheelDistance() / 2.0;
        double accel = motionProfile.getAcceleration(timePassed);
        double location = motionProfile.getLocation(timePassed);

        double ff = velocity / maxV + accel / maxA;
        double locPVal = locP * Position.normalizeAngle(location - chassis.getAngle());
        double velPLeft = velP * (perWheelVel - chassis.getLeftRate());
        double velPRight = velP * (-perWheelVel - chassis.getRightRate());

        chassis.moveMotors(
                clamp(ff + locPVal + velPLeft),
                -clamp(ff + locPVal + velPRight));
    }

    private double clamp(double inp) {
        return Math.copySign(Math.min(Math.abs(inp), 1.0), inp) * power;
    }

    @Override
    public void end(boolean interrupted) {
        double err = Math.toDegrees(chassis.getAngle() - end.getX());
        SmartDashboard.putNumber("Final Error", err);
        if (Math.abs(err) > 2 && !interrupted) {
            new ThreadedCommand(new DelicateTurn(end.getX()), Chassis.getInstance());
        }
    }

    @Override
    public boolean isFinished() {
        return overCount >= 3;
    }
}