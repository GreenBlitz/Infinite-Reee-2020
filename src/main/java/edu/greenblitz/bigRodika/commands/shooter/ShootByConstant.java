package edu.greenblitz.bigRodika.commands.shooter;

public class ShootByConstant extends ShooterCommand {

    protected double power;

    public ShootByConstant(double power) {
        this.power = power;
    }

    @Override
    public void execute() {
        shooter.shoot(power);
    }

    @Override
    public void end(boolean interrupted) {
//        shooter.shoot(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
