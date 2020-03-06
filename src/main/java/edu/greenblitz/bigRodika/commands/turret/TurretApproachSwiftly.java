package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.subsystems.Turret;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class TurretApproachSwiftly extends TurretCommand {

    public static final double SLOW_DOWN_BEGIN = Math.toRadians(15.0) / 2*Math.PI;
    public static final double SLOW_DOWN_END = Math.toRadians(8.0) / 2*Math.PI;
    public static final double MINIMUM_SPEED = 0.065;
    public static final double MAXIMUM_SPEED = 0.4;

    protected double target;
    protected ITolerance tolerance;
    public static final double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    public static final double X_OFFSET = SLOW_DOWN_END;
    public static final double Y_OFFSET = MINIMUM_SPEED;
    protected static final double DEFAULT_TOLERANCE = 0.003;//0.0015;

    public TurretApproachSwiftly(double target, ITolerance tol) {
        super();
        this.target = target;
        this.tolerance = tol;
    }

    public TurretApproachSwiftly(double target) {
        this(target, new AbsoluteTolerance(DEFAULT_TOLERANCE));
    }

    @Override
    public void execute() {
        turret.moveTurret(calculateVelocity(target - turret.getTurretLocation()));
    }

    @Override
    public boolean isFinished() {
        return tolerance.onTarget(target, turret.getTurretLocation());
    }

    @Override
    public void end(boolean interrupted) {
        turret.moveTurret(0);
    }

    public static double calculateVelocity(double error) {

        Turret.getInstance().putNumber("Error", error);

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
