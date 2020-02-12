package edu.greenblitz.bigRodika.commands.intake.roller;

public class RollByConstant extends RollerCommand {
    private double power;

    public RollByConstant(double power) {
        super();
        this.power = power;
    }

    @Override
    public void execute() {
        intake.moveRoller(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        intake.moveRoller(0);
    }
}
