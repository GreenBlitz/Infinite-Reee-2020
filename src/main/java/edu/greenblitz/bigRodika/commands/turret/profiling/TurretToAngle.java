package edu.greenblitz.bigRodika.commands.turret.profiling;

import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.profiling.ActuatorLocation;
import org.greenblitz.motion.profiling.MotionProfile1D;
import org.greenblitz.motion.profiling.Profiler1D;

public class TurretToAngle extends TurretCommand {

    private ActuatorLocation end;
    private MotionProfile1D motionProfile;
    private double power, locP, velP, maxV, maxA;
    private long t0;
    private boolean allowRedo;
    private double maxError;

    private int overCount;

    public TurretToAngle(double angleToTurnRad, double locP, double velP,
                         double maxV, double maxA,
                         double power, boolean allowRedo, double maxError) {
        this.locP = locP;
        this.velP = velP;
        this.maxA = maxA;
        this.maxV = maxV;
        this.power = power;
        this.end = new ActuatorLocation(angleToTurnRad, 0);
        this.allowRedo = allowRedo;
        this.maxError = maxError;
    }

    @Override
    public void initialize() {
        ActuatorLocation start = new ActuatorLocation(
                Turret.getInstance().getNormAngleRads(),
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
            turret.moveTurret(0);
            overCount++;
            return;
        }

        double velocity = motionProfile.getVelocity(timePassed);
        double accel = motionProfile.getAcceleration(timePassed);
        double location = motionProfile.getLocation(timePassed);

        double ff = velocity / maxV + accel / maxA;
        double locPVal = locP * Position.normalizeAngle(location - turret.getNormAngleRads());
        double velPVal = velP * (velocity - turret.getTurretSpeed()*2*Math.PI);

        turret.moveTurret(
                clamp(ff) + locPVal + velPVal);
    }

    private double clamp(double inp) {
        return Math.copySign(Math.min(Math.abs(inp), 1.0), inp) * power;
    }

    @Override
    public void end(boolean interrupted) {
        double err = Math.toDegrees(Position.normalizeAngle(turret.getNormAngleRads() - end.getX()));
        SmartDashboard.putNumber("Final Error", err);
        if (Math.abs(err) > maxError && !interrupted && allowRedo) {
            new ThreadedCommand(
                    new DelicateTurnTurret(end.getX()), turret).schedule();
        }
    }

    @Override
    public boolean isFinished() {
        return overCount >= 10;
    }
}
