package edu.greenblitz.bigRodika.commands.shooter;

import org.greenblitz.motion.pid.PIDObject;

public class ShootBySimplePid extends ShooterCommand {

    private PIDObject obj;
    private double target;

    public ShootBySimplePid(PIDObject obj, double target){
        this.obj = obj;
        this.target = target;
    }

    @Override
    public void initialize() {
        shooter.setPIDConsts(obj);
    }

    @Override
    public void execute() {
        shooter.setSpeedByPID(target);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
