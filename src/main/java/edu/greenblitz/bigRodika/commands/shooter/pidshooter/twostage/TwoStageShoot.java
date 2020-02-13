package edu.greenblitz.bigRodika.commands.shooter.pidshooter.twostage;

import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.WaitUntilShooterSpeedClose;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class TwoStageShoot extends SequentialCommandGroup {

    public TwoStageShoot() {


        addCommands(

                new ParallelRaceGroup(new WaitUntilShooterSpeedClose(2675, 100),
                        new ShootByConstant(1.0)),

                new ShootBySimplePid(new PIDObject(0.002, 0.00001 * 0, 0.00015, 0.48), 2675)
        );
    }

}
