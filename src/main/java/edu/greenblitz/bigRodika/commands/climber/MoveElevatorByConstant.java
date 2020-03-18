package edu.greenblitz.bigRodika.commands.climber;

public class MoveElevatorByConstant extends ClimberCommand {
    private double power;

    public MoveElevatorByConstant(double power) {
        super();
        this.power = power;
    }

    @Override
    public void execute() {
        climber.moveElevator(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        climber.moveElevator(0);
    }
}
