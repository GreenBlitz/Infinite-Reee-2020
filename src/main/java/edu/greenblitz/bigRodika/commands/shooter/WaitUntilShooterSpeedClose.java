package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;

public class WaitUntilShooterSpeedClose extends GBCommand {

    private double wantedVel;
    private double error;
    private int insuranceCount;
    private int count;

    public WaitUntilShooterSpeedClose(double vel, double error) {
        this(vel, error, 1);
    }

    public WaitUntilShooterSpeedClose(double vel, double error, int ins) {
        this.error = error;
        this.wantedVel = vel;
        this.insuranceCount = ins;
    }

    @Override
    public void initialize() {
        count = 0;
    }

    @Override
    public void execute() {
        if (wantedVel - Shooter.getInstance().getShooterSpeed() <= error) {
            count += 1;
        } else {
            count = 0;
        }
    }

    @Override
    public boolean isFinished() {
        return count >= insuranceCount;
    }
}
