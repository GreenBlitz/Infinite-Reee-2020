package edu.greenblitz.bigRodika.commands.climber;

public class MoveHookByConstant extends ClimberCommand {
    private double power;

    public MoveHookByConstant(double power) {
        super();
        this.power = power;
    }

    @Override
    public void execute() {
        climber.moveHook(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        climber.moveHook(0);
    }
}
