package edu.greenblitz.bigRodika.commands.funnel.inserter;

import edu.greenblitz.bigRodika.commands.funnel.FunnelCommand;

public abstract class shootingMethod extends FunnelCommand {

    public shootingMethod(){
        super();
        require(funnel.getInserter());
    }

}
