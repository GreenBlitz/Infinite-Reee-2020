package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;

public class WaitUntilAchivedVel extends GBCommand {
    private double wantedVel;

    public WaitUntilAchivedVel(double vel){
        this.wantedVel = vel;
    }

    @Override
    public boolean isFinished() {
        return(Math.abs(wantedVel - Shooter.getInstance().getShooterSpeed())<=0.15);
    }
}
