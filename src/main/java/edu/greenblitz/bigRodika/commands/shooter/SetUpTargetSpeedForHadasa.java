package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Shooter;
import org.greenblitz.motion.pid.PIDObject;

public class SetUpTargetSpeedForHadasa extends ShootBySimplePidForHadasa {
    private double target;
    private double maxError;
    private double t0, stableTime;
    private boolean stable = false;


    public SetUpTargetSpeedForHadasa(PIDObject obj, double target, double maxError, long stableTime) {
        super(obj, target);
        this.stableTime = stableTime;
        this.maxError = maxError;
        this.target = target;
    }

    @java.lang.Override
    public void initialize() {
        System.out.println("Started SetupTargetSpeed");
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

    @java.lang.Override
    public void end(boolean interrupted) {
        System.out.println("Ended SetupTargetSpeed");
    }
}
