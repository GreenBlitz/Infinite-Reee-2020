package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class ShooterCommand extends GBCommand {

    protected Shooter shooter;

    public ShooterCommand() {
        super(Shooter.getInstance());
        shooter = Shooter.getInstance();
    }

}
