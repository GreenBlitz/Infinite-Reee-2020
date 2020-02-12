package edu.greenblitz.bigRodika.commands.intake.roller;

import edu.greenblitz.bigRodika.commands.intake.IntakeCommand;

public abstract class RollerCommand extends IntakeCommand {
    public RollerCommand() {
        super();
        require(intake.getRoller());
    }
}
