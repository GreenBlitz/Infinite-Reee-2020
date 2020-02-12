package edu.greenblitz.bigRodika.commands.dome;

import org.greenblitz.motion.tolerance.ITolerance;

public class ApproachSwiftly extends DomeCommand {

    public final double SLOW_DOWN_BEGIN = 0.1;
    public final double SLOW_DOWN_END = 0.02;
    public final double MINIMUM_SPEED = 0.03;
    public final double MAXIMUM_SPEED = 0.1;

    private double target;
    private ITolerance tolerance;

    public ApproachSwiftly(double target, ITolerance tol){
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

    private final double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED)/(SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    private final double X_OFFSET = SLOW_DOWN_END;
    private final double Y_OFFSET = MINIMUM_SPEED;

    public double calculateVelocity(double error){

        double absError = Math.abs(error);
        double errorSign = Math.signum(error);

        if (absError <= SLOW_DOWN_END){
            return MINIMUM_SPEED * errorSign;
        }
        if (absError >= SLOW_DOWN_BEGIN){
            return MAXIMUM_SPEED * errorSign;
        }

        return ((absError - X_OFFSET)*SLOPE + Y_OFFSET) * errorSign;

    }

}
