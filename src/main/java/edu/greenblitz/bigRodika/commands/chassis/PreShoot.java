package edu.greenblitz.bigRodika.commands.chassis;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PreShoot extends SequentialCommandGroup {

    public PreShoot(){
        addCommands(new HexAlign(),new TurnToVision());
    }
}
