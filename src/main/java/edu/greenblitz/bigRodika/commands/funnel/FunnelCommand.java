package edu.greenblitz.bigRodika.commands.funnel;

import edu.greenblitz.bigRodika.subsystems.Funnel;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class FunnelCommand extends GBCommand {

    protected Funnel funnel;

    public FunnelCommand(){
        super(Funnel.getInstance());
        funnel = Funnel.getInstance();
    }

}
