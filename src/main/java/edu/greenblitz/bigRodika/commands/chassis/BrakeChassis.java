package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;

public class BrakeChassis extends ChassisCommand {
    @Override
    public void initialize() {
        Chassis.getInstance().moveMotors(0, 0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
