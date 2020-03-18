package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import org.greenblitz.motion.base.Point;

/**
 * @author Peleg Caduri
 */

public class MotionUtils {

    public static double[] planeryVisionDataToInnerHole(double[] visHex, double angle) {
        Point planeryVector = new Point(visHex[0], visHex[1]).translate(new Point(0, RobotMap.FieldData.HEX_DIST_FROM_INNER).rotate(angle));
        return new double[]{planeryVector.getX(), planeryVector.getY()};
    }

    public static double[] planeryVisionDataToInnerHole(double[] visHex) {
        return planeryVisionDataToInnerHole(visHex, Chassis.getInstance().getAngle());
    }

    public static double[] planeryVisionDataToInnerHole(double angle) {
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        double[] loc = VisionMaster.getInstance().getVisionLocation().toDoubleArray();
        if (!VisionMaster.getInstance().isLastDataValid()) return null;
        return planeryVisionDataToInnerHole(loc, angle);
    }

    public static double[] planeryVisionDataToInnerHole() {
        return planeryVisionDataToInnerHole(Chassis.getInstance().getAngle());
    }

    public static boolean isAngleGoodForShootingToInnerRads(double angle) {
        return angle <= Math.toRadians(RobotMap.FieldData.PHYSICAL_ANGLE_LIMIT) && angle <= Math.toRadians(RobotMap.Limbo2.ANGLE_SHOOTING_TO_INNER_DEG);
    }

    public static boolean isAngleGoodForShootingToInnerRads() {
        return isAngleGoodForShootingToInnerRads(Chassis.getInstance().getAngle());
    }

    public static double[] getSimulatedVisionLocation(double[] visHex, double turretAngle) {
        turretAngle *= -1;
        double x = RobotMap.Limbo2.Turret.TURRET_CAMERA_RADIUS;
        Point toOrigin = new Point(Math.sin(turretAngle), Math.cos(turretAngle)).scale(x).negate();
        Point fromOrigin = new Point(0, x);
        Point vector = (new Point(visHex[0], visHex[1])).rotate(-turretAngle)
                .translate(toOrigin)
                .translate(fromOrigin);

        return new double[]{vector.getX(), vector.getY()};
    }

    public static double[] getSimulatedVisionLocation(double[] visHex) {
        return getSimulatedVisionLocation(visHex,
                Turret.getInstance().getNormAngleRads()
        );
    }

    public static double[] getSimulatedVisionLocation(double turretAngle) {
        double[] loc = VisionMaster.getInstance().getVisionLocation().toDoubleArray();
        if (!VisionMaster.getInstance().isLastDataValid()) return null;
        return getSimulatedVisionLocation(loc, turretAngle);
    }

    public static double[] getSimulatedVisionLocation() {
        return getSimulatedVisionLocation(Turret.getInstance().getNormAngleRads());
    }

}
