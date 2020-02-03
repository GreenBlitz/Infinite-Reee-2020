package edu.greenblitz.bigRodika.commands.funnel.pusher;

import edu.greenblitz.bigRodika.commands.funnel.FunnelCommand;

public abstract class PusherCommand extends FunnelCommand {

    public PusherCommand(){
        super();
        require(funnel.getPusher());
    }

}
