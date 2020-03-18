package edu.greenblitz.bigRodika.commands.turret.help;

import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import org.opencv.core.Mat;

import java.util.function.Supplier;

public class JustGoToTheFuckingTarget extends TurretCommand {

    private double slowDownBegin, slowDownEnd, maximumSpeed, minimumSpeed, speedUpSlope;
    private double startOfRise = Double.NaN;
    private double target, tolerance;
    private Supplier<Double> targetSupplier;
    private long timeStartBeingOnTarget;

    private static final long MINIMUM_TIME_ON_TARGET = 100;

    public JustGoToTheFuckingTarget(
            Supplier<Double> targetSupp) {
        this(targetSupp,  Math.toRadians(1.0),
                Math.toRadians(20.0), Math.toRadians(5.0),
                0.5, 0.04,
                0.02/0.75);
    }

    public JustGoToTheFuckingTarget(
            Supplier<Double> targetSupp, double tolerance,
            double slowDownBegin, double slowDownEnd, double maximumSpeed, double minimumSpeed, double speedUpSlope) {
        this.targetSupplier = targetSupp;
        this.tolerance = tolerance;
        this.slowDownBegin = slowDownBegin;
        this.slowDownEnd = slowDownEnd;
        this.maximumSpeed = maximumSpeed;
        this.minimumSpeed = minimumSpeed;
        this.speedUpSlope = speedUpSlope;
    }

    @Override
    public void execute() {
        double error = target - turret.getNormAngleRads();
        if (Math.abs(error) <= tolerance){
            if (timeStartBeingOnTarget == 0) timeStartBeingOnTarget = System.currentTimeMillis();
            turret.moveTurret(0);
            return;
        }
        timeStartBeingOnTarget = 0;
        turret.moveTurret(calculateVelocity(error));
    }

    @Override
    public void initialize() {
        target = targetSupplier.get();
        startOfRise = Double.NaN;
        timeStartBeingOnTarget = 0;
    }

    @Override
    public void end(boolean interrupted) {
        turret.moveTurret(0);
    }

    public double calculateVelocity(double error){
        double absError = Math.abs(error);
        if (slowDownBegin < absError){
            return Math.copySign(maximumSpeed, error);
        }
        if (slowDownEnd < absError){
            return Math.copySign(
                    (maximumSpeed - minimumSpeed)*
                    (absError - slowDownEnd)/(slowDownBegin - slowDownEnd)
                    + minimumSpeed, error);
        }
        if (Double.isNaN(startOfRise)){
            startOfRise = System.currentTimeMillis();
        }
        double secondsPassed = (System.currentTimeMillis() - startOfRise)/1000.0;
        return Math.copySign(minimumSpeed + secondsPassed*speedUpSlope, error);
    }

    @Override
    public boolean isFinished() {
        return
                timeStartBeingOnTarget != 0 &&
                        System.currentTimeMillis() - timeStartBeingOnTarget > MINIMUM_TIME_ON_TARGET;
    }
}
