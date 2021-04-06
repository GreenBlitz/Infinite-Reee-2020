package edu.greenblitz.bigRodika.commands.turret.resets;

import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ResetEncoderWhenInSide extends TurretCommand {

    @Override
    public void initialize() {
        SmartDashboard.putString("Status", "Initialized");
    }

    @Override
    public void execute() {
        Turret.getInstance().moveTurret(0.3);
    }

    @Override
    public void end(boolean interrupted) {


        if (turret.getRawTicks() <= Turret.MIN_TICKS) {
            Turret.getInstance().moveTurretToSwitch(0.3);
            SmartDashboard.putString("Status", "override safeMove");
        }

        if (!interrupted) {
            SmartDashboard.putString("Status", "Done");
            Turret.getInstance().moveTurret(0);
            turret.resetEncoder(0);
        }
    }

    @Override
    public boolean isFinished() {
        return Turret.getInstance().isSwitchPressed() || turret.getRawTicks() <= Turret.MIN_TICKS;
    }
}