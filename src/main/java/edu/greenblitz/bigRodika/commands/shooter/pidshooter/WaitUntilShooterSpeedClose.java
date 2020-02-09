package edu.greenblitz.bigRodika.commands.shooter.pidshooter;

import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;

public class WaitUntilShooterSpeedClose extends GBCommand {
    private double wantedVel;
    private double error;


    public WaitUntilShooterSpeedClose(double vel, double error){
        this.error = error;
        this.wantedVel = vel;
    }

    @Override
    public boolean isFinished() {
        return (wantedVel - Shooter.getInstance().getShooterSpeed()<=error);
    }
}
