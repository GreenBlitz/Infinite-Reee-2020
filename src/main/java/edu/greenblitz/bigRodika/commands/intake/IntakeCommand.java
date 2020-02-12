package edu.greenblitz.bigRodika.commands.intake;

import edu.greenblitz.bigRodika.subsystems.Intake;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class IntakeCommand extends GBCommand {
    protected Intake intake;

    public IntakeCommand() {
        intake = Intake.getInstance();
    }
}
