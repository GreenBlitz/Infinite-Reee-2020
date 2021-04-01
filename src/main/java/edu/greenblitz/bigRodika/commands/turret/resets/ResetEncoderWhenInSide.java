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
        Turret.getInstance().moveTurret(0.1);
    }

    @Override
    public void end(boolean interrupted) {
        if (turret.getRawTicks() <= Turret.MIN_TICKS) {
            SmartDashboard.putBoolean("Override uunsafe", true);
            Turret.getInstance().moveTurretToSwitch(0.1);
        }

        if (!interrupted) {
            SmartDashboard.putBoolean("notIntrue;lkdsaj", true);
            Turret.getInstance().moveTurret(0);
            turret.resetEncoder(0);
        }
    }

    @Override
    public boolean isFinished() {
        return Turret.getInstance().isSwitchPressed() || turret.getRawTicks() <= Turret.MIN_TICKS;
    }
}