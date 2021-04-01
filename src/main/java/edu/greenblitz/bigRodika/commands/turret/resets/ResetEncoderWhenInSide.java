package edu.greenblitz.bigRodika.commands.turret.resets;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.commands.turret.StopTurret;
import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import edu.greenblitz.bigRodika.subsystems.Turret;

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
        if (!interrupted){
            Turret.getInstance().moveTurret(0);
            turret.resetEncoder(0);
        }
    }

    @Override
    public boolean isFinished() {
        return Turret.getInstance().isSwitchPressed();
    }
}