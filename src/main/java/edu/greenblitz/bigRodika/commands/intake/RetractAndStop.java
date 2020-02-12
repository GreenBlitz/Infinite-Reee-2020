package edu.greenblitz.bigRodika.commands.intake;

import edu.greenblitz.bigRodika.commands.intake.extender.RetractRoller;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class RetractAndStop extends SequentialCommandGroup {

    public RetractAndStop(){

        addCommands(new RetractRoller(), new StopRoller());

    }

}
