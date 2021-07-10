package edu.greenblitz.bigRodika.commands.turret.resets;

import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ResetEncoderWhenInSide extends TurretCommand {

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        Turret.getInstance().moveTurret(-0.1);
    }

    @Override
    public void end(boolean interrupted) {
        Turret.getInstance().moveTurret(0);
        Turret.getInstance().resetZeroValue();
    }

    @Override
    public boolean isFinished() {
        return Turret.getInstance().isSwitchPressed();
    }
}