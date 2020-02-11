package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class TurretCommand extends GBCommand {

    protected Turret turret;

    public TurretCommand() {
        super(Turret.getInstance());
        turret = Turret.getInstance();
    }
}
