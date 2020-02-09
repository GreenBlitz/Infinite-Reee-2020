package edu.greenblitz.bigRodika.commands.shooter.pidshooter;

import com.revrobotics.CANPIDController;
import edu.greenblitz.bigRodika.commands.generic.WaitMiliSeconds;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class ThreeStageShoot extends SequentialCommandGroup {

    public ThreeStageShoot(double target, double ff){


        addCommands(

                new ParallelRaceGroup(
                        new WaitUntilShooterSpeedClose(target, 100),
                        new ShootByConstant(1.0)
                ),

                new ParallelRaceGroup(
                        new ShootBySimplePid(
                                new PIDObject(0.0015,0.000004,0.0, ff), target
                        ),
                        new WaitUntilShooterSpeedClose(target, 8, 8) // Temp, replace by something better
                ),

                new StageThreePID(
                        new PIDObject(0.002,0.000004,0.00015, ff), target
                )
        );



    }

}
