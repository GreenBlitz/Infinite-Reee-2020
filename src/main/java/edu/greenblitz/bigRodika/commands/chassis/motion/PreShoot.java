package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.commands.chassis.turns.ChassisTurretCompensate;
import edu.greenblitz.bigRodika.commands.turret.TurretByVisionUntilStable;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.List;

public class PreShoot extends ParallelCommandGroup {

    private PreShoot(HexAlign align) {
        addCommands(
                new SequentialCommandGroup(
                        align
                        , new ChassisTurretCompensate()
                )
                , new TurretByVisionUntilStable(VisionMaster.Algorithm.HEXAGON)
        );
    }

    public PreShoot(double radius) {
        this(new HexAlign(radius, 0.2, 0.5, 0.1, 0.5));

    }

    public PreShoot(List<Double> radsAndCritPoints) {
        this(new HexAlign(radsAndCritPoints, 0.2, 0.5, 0.1, 0.5));
    }
}

