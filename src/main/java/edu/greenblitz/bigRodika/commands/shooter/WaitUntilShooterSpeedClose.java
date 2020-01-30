package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;

public class WaitUntilShooterSpeedClose extends GBCommand {
    private double wantedVel;
    private final double EPSILON = 0.15;


    public WaitUntilShooterSpeedClose(double vel){
        this.wantedVel = vel;
    }

    @Override
    public boolean isFinished() {
        return (wantedVel - Shooter.getInstance().getShooterSpeed()<=EPSILON);
    }
}
