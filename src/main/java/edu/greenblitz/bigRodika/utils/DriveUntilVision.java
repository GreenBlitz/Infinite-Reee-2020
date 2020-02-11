package edu.greenblitz.bigRodika.utils;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class DriveUntilVision extends GBCommand {


    private SmartJoystick joystick;

    public DriveUntilVision(SmartJoystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public void execute() {
        Chassis.getInstance().arcadeDrive(joystick.getAxisValue(SmartJoystick.Axis.LEFT_Y),
                joystick.getAxisValue(SmartJoystick.Axis.RIGHT_X));
    }

    @Override
    public boolean isFinished() {
        return VisionMaster.getInstance().isLastDataValid();
    }
}
