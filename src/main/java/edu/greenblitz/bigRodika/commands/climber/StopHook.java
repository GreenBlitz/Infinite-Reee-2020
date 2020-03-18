package edu.greenblitz.bigRodika.commands.climber;

public class StopHook extends ClimberCommand {
    @Override
    public void initialize() {
        climber.moveHook(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
