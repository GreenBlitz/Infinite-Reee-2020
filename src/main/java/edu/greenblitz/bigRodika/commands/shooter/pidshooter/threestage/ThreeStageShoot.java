package edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.WaitUntilShooterSpeedClose;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class ThreeStageShoot extends SequentialCommandGroup {

    public ThreeStageShoot(double target, double ff) {

        double kp = RobotMap.Limbo2.Shooter.SHOOTER_P;
        double ki = RobotMap.Limbo2.Shooter.SHOOTER_I;
        double kd = RobotMap.Limbo2.Shooter.SHOOTER_D;

        addCommands(

                new ParallelRaceGroup(
                        new WaitUntilShooterSpeedClose(target, 100),
                        new ShootByConstant(1.0)
                ),

                new ParallelRaceGroup(
                        new ShootBySimplePid(
                                new PIDObject(kp, ki, 0.0, ff), target
                        ),
                        new WaitUntilShooterSpeedClose(target, 8, 8) // Temp, replace by something better
                ),

                new StageThreePID(
                        new PIDObject(kp, 0, kd, ff), target
                )
        );


    }

}
