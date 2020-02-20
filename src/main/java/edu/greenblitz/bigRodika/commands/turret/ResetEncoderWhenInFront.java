package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeCommand;

public class ResetEncoderWhenInFront extends TurretCommand {

    @Override
    public void initialize() {
        turret.resetEncoder();
        RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD = 0.0;
    }
}
