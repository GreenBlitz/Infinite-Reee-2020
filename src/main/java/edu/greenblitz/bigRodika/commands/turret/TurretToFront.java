package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.RobotMap;

public class TurretToFront extends TurretApproachSwiftlyByRadiansRelative {

    public TurretToFront() {
        super(
                0
        );
    }

    @Override
    public void initialize() {
        angleToTurnRads = -turret.getNormAngleRads();
        super.initialize();
    }
}

