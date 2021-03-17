package edu.greenblitz.bigRodika.commands.intake.roller;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RollByConstant extends RollerCommand {
    private double power;

    public RollByConstant(double power) {
        super();
        this.power = power;
    }

    @Override
    public void execute() {
        SmartDashboard.putBoolean("RollByConstant", true);
        intake.moveRoller(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        SmartDashboard.putBoolean("RollByConstant", false);
        intake.moveRoller(0);
    }
}
