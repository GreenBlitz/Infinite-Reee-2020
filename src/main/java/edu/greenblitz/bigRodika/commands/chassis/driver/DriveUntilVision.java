package edu.greenblitz.bigRodika.commands.chassis.driver;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class DriveUntilVision extends ChassisCommand {


    private SmartJoystick joystick;

    public DriveUntilVision(SmartJoystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public void execute() {
        chassis.arcadeDrive(joystick.getAxisValue(SmartJoystick.Axis.LEFT_Y),
                joystick.getAxisValue(SmartJoystick.Axis.RIGHT_X));
    }

    @Override
    public boolean isFinished() {
        return VisionMaster.getInstance().isLastDataValid();
    }
}
