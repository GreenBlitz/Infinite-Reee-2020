package edu.greenblitz.bigRodika.commands.funnel.inserter;

import edu.greenblitz.bigRodika.commands.funnel.FunnelCommand;

public abstract class InserterCommand extends FunnelCommand {

    public InserterCommand(){
        super();
        require(funnel.getInserter());
    }

}
