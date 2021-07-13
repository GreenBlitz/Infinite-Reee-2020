package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class MoveByMoving extends SequentialCommandGroup {
    public MoveByMoving(){
        addCommands(
                new MoveFromLine()
        );
    }
}
