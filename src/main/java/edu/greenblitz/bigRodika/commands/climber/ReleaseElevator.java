package edu.greenblitz.bigRodika.commands.climber;

public class ReleaseElevator extends ClimberCommand {
    @Override
    public void initialize() {
        climber.release();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
