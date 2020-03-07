package edu.greenblitz.bigRodika.commands.turret.movebyp;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;

public class TurretToFront extends TurretApproachSwiftly {

    public TurretToFront() {
        super(
                RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD / RobotMap.Limbo2.Turret.NORMALIZER.getValue()
        );
    }

}

