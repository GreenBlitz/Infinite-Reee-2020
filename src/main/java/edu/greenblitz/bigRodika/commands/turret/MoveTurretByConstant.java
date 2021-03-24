package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MoveTurretByConstant extends TurretCommand {
    private double power;

    public MoveTurretByConstant(double power) {
        super();
        this.power = power;
    }

    @Override
    public void execute() {
        turret.moveTurret(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        turret.moveTurret(0);
    }
}
