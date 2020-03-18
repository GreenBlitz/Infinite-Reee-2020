package edu.greenblitz.bigRodika.commands.intake.extender;

import edu.greenblitz.bigRodika.commands.intake.IntakeCommand;

public abstract class ExtenderCommand extends IntakeCommand {

    public ExtenderCommand() {
        super();
        require(intake.getExtender());
    }
}
