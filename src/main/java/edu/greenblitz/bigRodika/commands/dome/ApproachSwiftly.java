package edu.greenblitz.bigRodika.commands.dome;

import org.greenblitz.motion.tolerance.ITolerance;

public class ApproachSwiftly extends DomeCommand {

    public double SLOW_DOWN_BEGIN = 0.1;
    public double SLOW_DOWN_END = 0.01;
    public double MINIMUM_SPEED = 0.12;
    public double MAXIMUM_SPEED = 1.0;

    private double target;
    private ITolerance tolerance;
    private double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    private double X_OFFSET = SLOW_DOWN_END;
    private double Y_OFFSET = MINIMUM_SPEED;

    public ApproachSwiftly(double target, ITolerance tol) {
        super();
        this.target = target;
        this.tolerance = tol;
    }

    @Override
    public void execute() {
        dome.safeMove(calculateVelocity(target - dome.getPotentiometerValue()));
    }

    @Override
    public boolean isFinished() {
        return tolerance.onTarget(target, dome.getPotentiometerValue());
    }

    @Override
    public void end(boolean interrupted) {
        dome.moveMotor(0);
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
