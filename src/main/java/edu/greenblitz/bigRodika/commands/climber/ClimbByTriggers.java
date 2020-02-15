package edu.greenblitz.bigRodika.commands.climber;

import edu.greenblitz.gblib.hid.SmartJoystick;

public class ClimbByTriggers extends ClimberCommand {

    private SmartJoystick stick;
    private double multiplier;

    public ClimbByTriggers(SmartJoystick s, double mult){
        stick = s;
        multiplier = mult;
    }

    @Override
    public void execute() {
        climber.moveElevator(multiplier*(stick.getAxisValue(SmartJoystick.Axis.RIGHT_TRIGGER) -
                                stick.getAxisValue(SmartJoystick.Axis.LEFT_TRIGGER)));
    }


}
