package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.funnel.BetterFunnelCommand;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.commands.turret.resets.ResetEncoderWhenInSide;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShootNGo extends SequentialCommandGroup {
    // Angle: 0.554 rad
    // 1906 rpm

    public ShootNGo() {
        addCommands(
                new ResetEncoderWhenInSide(),
                new TurretApproachSwiftly(0.127), // FIXME: this number would probably not work
                new DomeApproachSwiftly(0.554),
                new FullyAutoThreeStage(1906),
                new BetterFunnelCommand(1906)

        );
    }

}
