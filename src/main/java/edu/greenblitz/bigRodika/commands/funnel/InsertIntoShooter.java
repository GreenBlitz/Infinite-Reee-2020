package edu.greenblitz.bigRodika.commands.funnel;

import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class InsertIntoShooter extends SequentialCommandGroup {

    public InsertIntoShooter(double insertionConst, double pushConst) {

        addCommands(

                new WaitUntilCommand(() -> Shooter.getInstance().isPreparedToShoot()),
                new ParallelCommandGroup(

                        new InsertByConstant(insertionConst),
                        new PushByConstant(pushConst)

                )

        );

    }

}
