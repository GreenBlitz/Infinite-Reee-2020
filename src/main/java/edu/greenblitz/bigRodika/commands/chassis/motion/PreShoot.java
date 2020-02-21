package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.commands.turret.TurretToFront;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import java.util.List;

public class PreShoot extends ParallelCommandGroup {

    public PreShoot(double radius) {
        HexAlign hexAlign = new HexAlign(radius, 0.2, 0.5, 0.1, 0.5);
        addCommands(

                hexAlign,
                new TurretByVision(VisionMaster.Algorithm.HEXAGON)

        );

    }

    public PreShoot(List<Double> radsAndCritPoints, boolean useTurret) {
        HexAlign hexAlign = new HexAlign(radsAndCritPoints, 0.2, 0.5, 0.1, 0.5);
        addCommands(hexAlign,

                new TurretByVision(VisionMaster.Algorithm.HEXAGON));
    }
}

