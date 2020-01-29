package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class ArcadeDrive extends ChassisCommand {

    private SmartJoystick joystick;

    public ArcadeDrive(SmartJoystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public void execute() {
        chassis.arcadeDrive(joystick.getAxisValue(SmartJoystick.Axis.LEFT_Y),
                                          joystick.getAxisValue(SmartJoystick.Axis.RIGHT_X));
    }
}
