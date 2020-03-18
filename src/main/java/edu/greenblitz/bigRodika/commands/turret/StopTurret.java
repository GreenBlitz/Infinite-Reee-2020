package edu.greenblitz.bigRodika.commands.turret;

public class StopTurret extends TurretCommand {
    @Override
    public void initialize() {
        turret.moveTurret(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
