package edu.greenblitz.bigRodika.commands.funnel;

import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByDifferentConstants;
import edu.greenblitz.bigRodika.utils.WaitMiliSeconds;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class BetterFunnelCommand extends SequentialCommandGroup {
    public BetterFunnelCommand() {
        addCommands(
                new ParallelRaceGroup(
                        new PushByDifferentConstants(0.8, -0.3),
                        new WaitMiliSeconds(500)
                ),
                new PushByDifferentConstants(0.8, 0.2)
        );
    }
}
