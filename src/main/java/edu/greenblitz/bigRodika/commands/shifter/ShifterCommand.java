package edu.greenblitz.bigRodika.commands.shifter;

import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class ShifterCommand extends GBCommand {

    Shifter shifter;

    public ShifterCommand() {
        super(Shifter.getInstance());
        shifter = Shifter.getInstance();
    }

}
