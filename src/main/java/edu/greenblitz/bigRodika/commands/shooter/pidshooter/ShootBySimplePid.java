package edu.greenblitz.bigRodika.commands.shooter.pidshooter;

import edu.greenblitz.bigRodika.commands.shooter.ShooterCommand;
import org.greenblitz.debug.RemoteCSVTarget;
import org.greenblitz.motion.pid.PIDObject;

public class ShootBySimplePid extends ShooterCommand {

    private PIDObject obj;
    private RemoteCSVTarget logger;
    private double target;
    private long tStart;

    public ShootBySimplePid(PIDObject obj, double target){
        this.obj = obj;
        this.obj.setKf(this.obj.getKf() / target);
        this.target = target;
        this.logger = RemoteCSVTarget.initTarget("FlyWheelVel", "time", "vel");
    }

    @Override
    public void initialize() {
        shooter.getPIDController().setIAccum(0);
        shooter.setPIDConsts(obj);
        tStart = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        shooter.setSpeedByPID(target);
        logger.report((System.currentTimeMillis() - tStart)/1000.0, shooter.getShooterSpeed());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
