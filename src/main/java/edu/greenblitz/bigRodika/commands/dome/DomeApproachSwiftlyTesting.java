package edu.greenblitz.bigRodika.commands.dome;

import org.greenblitz.motion.tolerance.ITolerance;

public class DomeApproachSwiftlyTesting extends DomeCommand {

    public double SLOW_DOWN_BEGIN = 0.1;
    public double SLOW_DOWN_END = 0.01;
    public double MINIMUM_SPEED = 0.12;
    public double MAXIMUM_SPEED = 0.6;

    private double target;
    private ITolerance tolerance;
    private double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    private double X_OFFSET = SLOW_DOWN_END;
    private double Y_OFFSET = MINIMUM_SPEED;

    public DomeApproachSwiftlyTesting(double target, ITolerance tol) {
        super();
        this.target = target;
        this.tolerance = tol;
        dome.putNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        dome.putNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        dome.putNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        dome.putNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
        dome.putNumber("TARGET", target);
    }

    @Override
    public void initialize() {
        SLOW_DOWN_BEGIN = dome.getNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        SLOW_DOWN_END = dome.getNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        MINIMUM_SPEED = dome.getNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        MAXIMUM_SPEED = dome.getNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
    }

    @Override
    public void execute() {
        SLOW_DOWN_BEGIN = dome.getNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        SLOW_DOWN_END = dome.getNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        MINIMUM_SPEED = dome.getNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        MAXIMUM_SPEED = dome.getNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
        target = dome.getNumber("TARGET", target);
        dome.safeMove(calculateVelocity(target - dome.getEncoderValue()));
    }

    @Override
    public boolean isFinished() {
        return tolerance.onTarget(target, dome.getEncoderValue());
    }

    @Override
    public void end(boolean interrupted) {
        dome.safeMove(0);
    }

    public double calculateVelocity(double error) {

        SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
        X_OFFSET = SLOW_DOWN_END;
        Y_OFFSET = MINIMUM_SPEED;

        dome.putNumber("Error", error);

        double absError = Math.abs(error);
        double errorSign = Math.signum(error);

        if (absError <= SLOW_DOWN_END) {
            return MINIMUM_SPEED * errorSign;
        }
        if (absError >= SLOW_DOWN_BEGIN) {
            return MAXIMUM_SPEED * errorSign;
        }

        return ((absError - X_OFFSET) * SLOPE + Y_OFFSET) * errorSign;

    }

}
