package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Shooter;
import org.greenblitz.motion.pid.PIDObject;

public class SetUpTargetSpeed extends ShootBySimplePid {
    private double target;
    private double maxError;
    private double t0, stableTime;
    private boolean stable = false;


    public SetUpTargetSpeed(PIDObject obj, double target, double maxError, long stableTime) {
        super(obj, target);
        this.stableTime = stableTime;
        this.maxError = maxError;
        this.target = target;
    }

    private boolean isOnTarget(){
        return Math.abs(Shooter.getInstance().getShooterSpeed() - target) <= maxError;
    }

    @Override
    public boolean isFinished() {
         if (!isOnTarget()) {
            stable = false;
            return false;
        }
        if (!stable) {
            t0 = System.currentTimeMillis();
            stable = true;
        }
        return (System.currentTimeMillis() - t0 >= stableTime);
    }
}
