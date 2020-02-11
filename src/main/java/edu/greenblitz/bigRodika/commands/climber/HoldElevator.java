package edu.greenblitz.bigRodika.commands.climber;

public class HoldElevator extends ClimberCommand {
    @Override
    public void initialize() {
        climber.hold();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
