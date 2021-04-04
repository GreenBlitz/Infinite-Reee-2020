package edu.greenblitz.bigRodika.commands.shooter;

public class StopShooter extends ShooterCommand {

    @Override
    public void initialize() {
        shooter.toBrake();
        shooter.shoot(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
