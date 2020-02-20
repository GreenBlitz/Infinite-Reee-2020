package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.utils.VisionMaster;

public class TurretByVision extends TurretCommand {

    private VisionMaster.Algorithm algorithm;

    public TurretByVision(VisionMaster.Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public static final double SLOW_DOWN_BEGIN = TurretApproachSwiftly.SLOW_DOWN_BEGIN * 2 * Math.PI;
    public static final double SLOW_DOWN_END = TurretApproachSwiftly.SLOW_DOWN_END * 2 * Math.PI;
    public static final double MINIMUM_SPEED = TurretApproachSwiftly.MINIMUM_SPEED;
    public static final double MAXIMUM_SPEED = TurretApproachSwiftly.MAXIMUM_SPEED;

    public static final double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    public static final double X_OFFSET = SLOW_DOWN_END;
    public static final double Y_OFFSET = MINIMUM_SPEED;

    @Override
    public void initialize() {
        algorithm.setAsCurrent();
    }

    @Override
    public void execute() {
        double[] diff = VisionMaster.getInstance().getVisionLocation().toDoubleArray();

        if (!VisionMaster.getInstance().isLastDataValid()) {
            turret.moveTurret(0);
            return;
        }

        turret.moveTurret(calculateVelocity(
                Math.atan(diff[0] / diff[1])));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        turret.moveTurret(0);
    }

    public double calculateVelocity(double error) {

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
