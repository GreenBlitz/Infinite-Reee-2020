package edu.greenblitz.bigRodika.commands.funnel.pusher;

public class StopPusher extends PusherCommand {

    @Override
    public void initialize() {
        funnel.movePusher(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
