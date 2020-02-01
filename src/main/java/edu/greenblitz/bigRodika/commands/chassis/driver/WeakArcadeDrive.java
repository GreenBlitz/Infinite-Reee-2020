package edu.greenblitz.bigRodika.commands.chassis.driver;

import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class WeakArcadeDrive extends ChassisCommand {

    private SmartJoystick joystick;
    private double power;

    public WeakArcadeDrive(SmartJoystick joystick, double pow) {
        this.joystick = joystick;
        this.power = pow;
    }

    @Override
    public void execute() {
        chassis.arcadeDrive(power*joystick.getAxisValue(SmartJoystick.Axis.LEFT_Y),
                power*joystick.getAxisValue(SmartJoystick.Axis.RIGHT_X));
    }
}
