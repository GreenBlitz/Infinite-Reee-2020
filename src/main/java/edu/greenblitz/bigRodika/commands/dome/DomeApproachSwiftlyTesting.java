package edu.greenblitz.bigRodika.commands.dome;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class DomeApproachSwiftlyTesting extends DomeCommand {

    public double SLOW_DOWN_BEGIN = 0.1;
    public double SLOW_DOWN_END = 0.01;
    public double MINIMUM_SPEED = 0.12;
    public double MAXIMUM_SPEED = 0.6;

    private double tol;
    private double target;
    private ITolerance tolerance;
    private double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    private double X_OFFSET = SLOW_DOWN_END;
    private double Y_OFFSET = MINIMUM_SPEED;

    public DomeApproachSwiftlyTesting(double target, double tol) {
        super();
        this.target = target;
        this.tolerance = new AbsoluteTolerance(tol);
        SmartDashboard.putNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        SmartDashboard.putNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        SmartDashboard.putNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        SmartDashboard.putNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
        SmartDashboard.putNumber("TARGET", target);
        SmartDashboard.putNumber("DOME_TOLERANCE", tol);
    }

    @Override
    public void initialize() {
        SLOW_DOWN_BEGIN = SmartDashboard.getNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        SLOW_DOWN_END = SmartDashboard.getNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        MINIMUM_SPEED = SmartDashboard.getNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        MAXIMUM_SPEED = SmartDashboard.getNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
    }

    @Override
    public void execute() {
        SLOW_DOWN_BEGIN = SmartDashboard.getNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        SLOW_DOWN_END = SmartDashboard.getNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        MINIMUM_SPEED = SmartDashboard.getNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        MAXIMUM_SPEED = SmartDashboard.getNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
        target = SmartDashboard.getNumber("TARGET", target);
        tolerance = new AbsoluteTolerance(SmartDashboard.getNumber("DOME_TOLERANCE", tol));
        dome.safeMove(calculateVelocity(target - dome.getPotentiometerValue()));
    }

    @Override
    public boolean isFinished() {
        return tolerance.onTarget(target, dome.getPotentiometerValue());
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
