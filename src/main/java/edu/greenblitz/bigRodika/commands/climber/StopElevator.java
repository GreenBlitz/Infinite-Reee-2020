package edu.greenblitz.bigRodika.commands.climber;

public class StopElevator extends ClimberCommand {
    @Override
    public void initialize() {
        climber.moveElevator(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
