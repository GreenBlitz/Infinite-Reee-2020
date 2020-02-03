package edu.greenblitz.bigRodika.commands.complex;

import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class SimpleShoot extends ParallelCommandGroup {

    public SimpleShoot(double pushPower, double insertConst){

        addCommands(new ShootByConstant(0.3),
                new SequentialCommandGroup(new WaitCommand(1),
                        new ParallelCommandGroup(new PushByConstant(pushPower), new InsertByConstant(insertConst))));

    }

}
