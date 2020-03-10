package edu.greenblitz.bigRodika.commands.climber;

import org.greenblitz.motion.pid.PIDObject;

public class HookByPID extends ClimberCommand {
    protected PIDObject obj;
    protected double target;

    public HookByPID(PIDObject obj, double target) {
        this.obj = obj;
        this.target = target;
    }

    @Override
    public void execute() {
    }
}
