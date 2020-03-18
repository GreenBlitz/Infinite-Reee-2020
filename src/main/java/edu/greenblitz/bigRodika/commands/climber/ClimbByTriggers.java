package edu.greenblitz.bigRodika.commands.climber;

import edu.greenblitz.gblib.hid.SmartJoystick;

public class ClimbByTriggers extends ClimberCommand {

    private SmartJoystick elevatorStick;
    private SmartJoystick hookStick;
    private double elevatorMultiplier;
    private double hookMultiplier;

    public ClimbByTriggers(SmartJoystick elevatorStick, SmartJoystick hookStick, double elevatorMult, double hookMult) {
        this.elevatorStick = elevatorStick;
        this.hookStick = hookStick;
        this.elevatorMultiplier = elevatorMult;
        this.hookMultiplier = hookMult;
    }

    @Override
    public void execute() {
        climber.moveElevator(elevatorMultiplier * (elevatorStick.getAxisValue(SmartJoystick.Axis.RIGHT_TRIGGER) -
                elevatorStick.getAxisValue(SmartJoystick.Axis.LEFT_TRIGGER)));
        climber.safeMoveHook(hookMultiplier * (hookStick.getAxisValue(SmartJoystick.Axis.LEFT_TRIGGER) - hookStick.getAxisValue(SmartJoystick.Axis.RIGHT_TRIGGER)));
    }


}
