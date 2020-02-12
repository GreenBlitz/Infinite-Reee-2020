package edu.greenblitz.bigRodika.commands.funnel.pusher;

public class PushByConstant extends PusherCommand {

    private double power;

    public PushByConstant(double power){
        super();
        this.power = power;
    }

    @Override
    public void execute() {
        funnel.movePusher(power);
    }

    @Override
    public void end(boolean interrupted) {
        funnel.movePusher(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
