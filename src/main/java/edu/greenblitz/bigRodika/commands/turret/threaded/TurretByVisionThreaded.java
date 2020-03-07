package edu.greenblitz.bigRodika.commands.turret.threaded;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.threading.IThreadable;

public class TurretByVisionThreaded implements IThreadable {

    private VisionMaster.Algorithm algorithm;

    public TurretByVisionThreaded(VisionMaster.Algorithm algorithm) {
        this.algorithm = algorithm;
        this.turret = Turret.getInstance();
    }

    public static final double SLOW_DOWN_BEGIN = TurretApproachSwiftly.SLOW_DOWN_BEGIN * 2 * Math.PI;
    public static final double SLOW_DOWN_END = 0.0;
    public static final double MINIMUM_SPEED = 0.04;
    public static final double MAXIMUM_SPEED = TurretApproachSwiftly.MAXIMUM_SPEED;

    public static final double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    public static final double X_OFFSET = SLOW_DOWN_END;
    public static final double Y_OFFSET = MINIMUM_SPEED;

    protected Turret turret;

    @Override
    public void atInit() {
        algorithm.setAsCurrent();
        System.out.println("Start TurretByVision");
    }

    @Override
    public void run() {

        if (!VisionMaster.getInstance().isLastDataValid()) {
            turret.moveTurret(0);
            return;
        }

        if (isFinished()){
            turret.moveTurret(0);
            return;
        }

        turret.moveTurret(calculateVelocity(
                Math.toRadians(VisionMaster.getInstance().getVisionLocation().getRelativeAngle())
                ));
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

    @Override
    public boolean isFinished() {
        return Math.abs(
                VisionMaster.getInstance().getVisionLocation().getRelativeAngle()
                - Math.toDegrees(RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET)) < 1.0;
    }

    @Override
    public void atEnd() {
        turret.moveTurret(0);
    }

}
