package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class ArcadeDriveUntilVision extends ArcadeDrive {
    public ArcadeDriveUntilVision(SmartJoystick joystick) {
        super(Chassis.getInstance(), joystick);
    }

    @Override
    public boolean isFinished() {
        return VisionMaster.getInstance().isLastDataValid();
    }
}
