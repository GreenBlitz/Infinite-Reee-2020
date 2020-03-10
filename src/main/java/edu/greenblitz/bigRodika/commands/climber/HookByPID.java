package edu.greenblitz.bigRodika.commands.climber;

import org.greenblitz.motion.pid.PIDController;
import org.greenblitz.motion.pid.PIDObject;

public class HookByPID extends ClimberCommand {
    private PIDController controller;

    public HookByPID(PIDObject obj) {
        controller = new PIDController(obj);
    }

    @Override
    public void execute() {
        climber.moveHook(controller.calculatePID(climber.getElevatorPosition()));
    }

    @Override
    public void end(boolean interrupted) {
        climber.moveHook(0);
    }
}
