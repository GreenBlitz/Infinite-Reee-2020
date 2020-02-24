package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.RobotMap;

public class ResetEncoderWhenInSide extends TurretCommand {

    @Override
    public void initialize() {
        turret.resetEncoder((int) RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_NEGATIVE_90);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
