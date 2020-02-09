package edu.greenblitz.bigRodika.commands.intake.extender;

import edu.greenblitz.bigRodika.commands.intake.IntakeCommand;
import edu.greenblitz.bigRodika.subsystems.Intake;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class ExtenderCommand extends IntakeCommand {
    public ExtenderCommand() {
        super();
        require(intake.getExtender());
    }
}
