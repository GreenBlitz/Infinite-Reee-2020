package edu.greenblitz.bigRodika.commands.dome;

import edu.greenblitz.bigRodika.subsystems.Dome;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class DomeCommand extends GBCommand {

    protected Dome dome;

    public DomeCommand() {
        require(Dome.getInstance());
        dome = Dome.getInstance();
    }

}
