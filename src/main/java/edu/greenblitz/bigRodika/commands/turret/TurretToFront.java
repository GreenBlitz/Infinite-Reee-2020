package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.RobotMap;

public class TurretToFront extends TurretApproachSwiftly {

    public TurretToFront() {
        super(RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD / RobotMap.Limbo2.Turret.NORMALIZER.getValue());
    }

}

