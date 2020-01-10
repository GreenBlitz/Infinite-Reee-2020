package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class ArcadeDrive extends GBCommand {

    private SmartJoystick joystick;

    public ArcadeDrive(SmartJoystick joystick) {
        super(Chassis.getInstance());
        this.joystick = joystick;
    }

    @Override
    public void execute() {
        Chassis.getInstance().arcadeDrive(joystick.getAxisValue(SmartJoystick.Axis.LEFT_Y),
                                          joystick.getAxisValue(SmartJoystick.Axis.RIGHT_X));
    }
}
