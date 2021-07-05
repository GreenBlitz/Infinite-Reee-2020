package edu.greenblitz.bigRodika.commands.dome;

import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class DomeApproachSwiftly extends DomeCommand {

    public double SLOW_DOWN_BEGIN = 0.1;
    public double SLOW_DOWN_END = 0.04;
    public double MINIMUM_SPEED = 0.1;
    public double MAXIMUM_SPEED = 0.3;

    private double target;
    private ITolerance tolerance;
    private double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    private double X_OFFSET = SLOW_DOWN_END;
    private double Y_OFFSET = MINIMUM_SPEED;

    private static final double DEFAULT_TOLERENCE = 0.005;

    public DomeApproachSwiftly(double target, ITolerance tol) {
        super();
        this.target = target;
        this.tolerance = tol;
    }

    public DomeApproachSwiftly(double target) {
        this(target, new AbsoluteTolerance(DEFAULT_TOLERENCE));
    }

    @Override
    public void execute() {
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
