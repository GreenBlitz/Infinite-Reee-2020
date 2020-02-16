package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class ChassisCommand extends GBCommand {

    protected Chassis chassis;

    public ChassisCommand() {
        super(Chassis.getInstance());
        chassis = Chassis.getInstance();
    }

}
