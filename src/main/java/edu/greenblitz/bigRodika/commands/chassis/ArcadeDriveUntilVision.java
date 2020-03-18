package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class ArcadeDriveUntilVision extends ArcadeDrive {
    public ArcadeDriveUntilVision(SmartJoystick joystick) {
        super(joystick);
    }

    @Override
    public boolean isFinished() {
        return VisionMaster.getInstance().isLastDataValid();
    }
}
