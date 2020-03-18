package edu.greenblitz.bigRodika.commands.turret.resets;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.turret.TurretCommand;

public class ResetEncoderWhenInFront extends TurretCommand {

    @Override
    public void initialize() {
        turret.resetEncoder((int) RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD);
    }
}
