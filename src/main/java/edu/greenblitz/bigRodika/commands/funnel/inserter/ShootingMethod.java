package edu.greenblitz.bigRodika.commands.funnel.inserter;

import edu.greenblitz.bigRodika.commands.funnel.FunnelCommand;

public abstract class ShootingMethod extends FunnelCommand {

    public ShootingMethod(){
        super();
        require(funnel.getInserter());
    }

}
