package edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.autonomous;

import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.WaitUntilShooterSpeedClose;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.StageThreePID;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class ThreeStageForAutonomous extends SequentialCommandGroup {

    public ThreeStageForAutonomous(double target, double ff) {

        // 0.6 = 3100
        // 0.48 = 2800

        addCommands(

                new ParallelRaceGroup(
                        new WaitUntilShooterSpeedClose(target, 100),
                        new ShootByConstant(1.0)
                ),

                new ParallelRaceGroup(
                        new ShootBySimplePid(
                                new PIDObject(0.0015, 0.000004, 0.0, ff), target
                        ),
                        new SequentialCommandGroup(

                                new WaitUntilShooterSpeedClose(target, 5, 15)
                        )
                ),

                new ParallelCommandGroup(
                        new StageThreePID(
                                new PIDObject(0.002, 0.000004, 0.00015, ff), target
                        )
                )
        );


    }


}
