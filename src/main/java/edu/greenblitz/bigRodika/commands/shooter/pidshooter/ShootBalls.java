package edu.greenblitz.bigRodika.commands.shooter.pidshooter;

import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class ShootBalls extends SequentialCommandGroup {

    public ShootBalls(){


        addCommands(

                new ParallelRaceGroup(new WaitUntilShooterSpeedClose(3100, 100),
                new ShootByConstant(1.0)),

                new ShootBySimplePid(new PIDObject(0.002,0.00001*0,0.0001,0.6), 3100));
    }

}
