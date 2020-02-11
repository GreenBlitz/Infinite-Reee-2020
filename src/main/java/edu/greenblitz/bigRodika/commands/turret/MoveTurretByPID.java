package edu.greenblitz.bigRodika.commands.turret;

import org.greenblitz.motion.pid.PIDObject;

public class MoveTurretByPID extends TurretCommand {
    protected static final int PID_INDEX = 0;
    protected double target;

    public MoveTurretByPID(PIDObject obj, double targetVelocity) {
        turret.configurePID(PID_INDEX, obj);
        target = targetVelocity;
    }

    @Override
    public void initialize() {
        turret.selectPIDLoop(PID_INDEX);
    }

    @Override
    public void execute() {
        turret.moveTurretByPID(target);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        turret.moveTurret(0);
    }
}
