package edu.greenblitz.bigRodika.commands.turret.movebyp;

import edu.greenblitz.bigRodika.RobotMap;

public class TurretToBack extends TurretApproachSwiftly {

    public TurretToBack() {
        super(
                RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD / RobotMap.Limbo2.Turret.NORMALIZER.getValue() - 0.5
        );
    }

}

