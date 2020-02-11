package edu.greenblitz.bigRodika.commands.climber;

import edu.greenblitz.bigRodika.subsystems.Climber;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class ClimberCommand extends GBCommand {
    protected Climber climber;

    public ClimberCommand() {
        require(Climber.getInstance());
        climber = Climber.getInstance();
    }
}
