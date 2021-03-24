package edu.greenblitz.bigRodika.commands.turret.movebyp;

import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.tolerance.ITolerance;

public class TurretApproachSwiftlyTesting extends TurretCommand {

    public double SLOW_DOWN_BEGIN = 0.1;
    public double SLOW_DOWN_END = 0.01;
    public double MINIMUM_SPEED = 0.12;
    public double MAXIMUM_SPEED = 1.0;

    private double target;
    private ITolerance tolerance;
    private double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    private double X_OFFSET = SLOW_DOWN_END;
    private double Y_OFFSET = MINIMUM_SPEED;

    public TurretApproachSwiftlyTesting(double target, ITolerance tol) {
        super();
        this.target = target;
        this.tolerance = tol;
        turret.putNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        turret.putNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        turret.putNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        turret.putNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
        turret.putNumber("TARGET", target);
    }

    @Override
    public void initialize() {
        SLOW_DOWN_BEGIN = turret.getNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        SLOW_DOWN_END = turret.getNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        MINIMUM_SPEED = turret.getNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        MAXIMUM_SPEED = turret.getNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
    }

    @Override
    public void execute() {
        SLOW_DOWN_BEGIN = turret.getNumber("SLOW_DOWN_BEGIN", SLOW_DOWN_BEGIN);
        SLOW_DOWN_END = turret.getNumber("SLOW_DOWN_END", SLOW_DOWN_END);
        MINIMUM_SPEED = turret.getNumber("MINIMUM_SPEED", MINIMUM_SPEED);
        MAXIMUM_SPEED = turret.getNumber("MAXIMUM_SPEED", MAXIMUM_SPEED);
        target = turret.getNumber("TARGET", target);
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

    public double calculateVelocity(double error) {

        SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
        X_OFFSET = SLOW_DOWN_END;
        Y_OFFSET = MINIMUM_SPEED;

        turret.putNumber("Error", error);

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
