package edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.test;

import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.WaitUntilShooterSpeedClose;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.StageThreePID;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class ThreeStageTesting extends SequentialCommandGroup {

    public ThreeStageTesting() {

        // 0.6 = 3100
        // 0.48 = 2800

        double target = 2800;//Shooter.getInstance().getNumber("testing_target", 0);
        double ff = 0.48;//Shooter.getInstance().getNumber("testing_ff", 0);

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

                                new WaitUntilShooterSpeedClose(target, 7, 10)
                        )
                ),

                new ParallelCommandGroup(
                        new StageThreePID(
                                new PIDObject(0.002, 0.000004, 0.00015, ff), target
                        )
                )
        );


    }

    public static class Starter extends GBCommand {

        @Override
        public void initialize() {
            new ThreeStageTesting().schedule();
        }

        @Override
        public boolean isFinished() {
            return true;
        }
    }


}
