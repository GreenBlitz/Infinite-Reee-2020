package edu.greenblitz.bigRodika.commands.shooter;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShootByConstant extends ShooterCommand {

    protected double power;
    public ShootByConstant(double power) {
        this.power = power;
    }



    @Override
    public void execute() {
        shooter.shoot(power);
        SmartDashboard.putNumber("shooter", shooter.getShooterSpeed());
    }

    @Override
    public void end(boolean interrupted) {
        shooter.shoot(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
