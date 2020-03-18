package edu.greenblitz.bigRodika.commands.shooter.pidshooter;

import edu.greenblitz.bigRodika.commands.shooter.ShooterCommand;
import org.greenblitz.debug.RemoteCSVTarget;
import org.greenblitz.motion.pid.PIDObject;

public class ShootByDashboard extends ShooterCommand {

    private PIDObject obj;
    private RemoteCSVTarget logger;
    private double target;
    private long tStart;

    public ShootByDashboard(double target) {
        this.target = target;
        this.logger = RemoteCSVTarget.initTarget("FlyWheelVel", "time", "vel");
    }

    @Override
    public void initialize() {
        shooter.getPIDController().setIAccum(0);

        this.target = shooter.getNumber("testing_target", 100);
        this.obj = new PIDObject(shooter.getNumber("p", 0.01),
                shooter.getNumber("i", 0),
                shooter.getNumber("d", 0),
                shooter.getNumber("f", 0.6));
        this.obj.setKf(this.obj.getKf() / target);

        shooter.setPIDConsts(obj);
        tStart = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        shooter.setSpeedByPID(target);
        logger.report((System.currentTimeMillis() - tStart) / 1000.0, shooter.getShooterSpeed());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
