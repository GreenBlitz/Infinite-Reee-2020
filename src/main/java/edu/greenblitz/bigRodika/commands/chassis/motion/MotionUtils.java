package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import org.greenblitz.motion.base.Point;

/**
 * @author Peleg Caduri
 */

public class MotionUtils {

    public static double[] planeryVisionDataToInnerHole(double[] visHex, double angle) {
        Point planeryVector = new Point(visHex[0], visHex[1]).translate(new Point(0, RobotMap.FieldData.HexDistFromInnerHole).rotate(angle));
        return new double[]{planeryVector.getX(), planeryVector.getY()};
    }

    public static double[] planeryVisionDataToInnerHole(double[] visHex){
        return planeryVisionDataToInnerHole(visHex, Chassis.getInstance().getAngle());
    }

    public static double[] planeryVisionDataToInnerHole(double angle){
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        double[] loc = VisionMaster.getInstance().getVisionLocation().toDoubleArray();
        if(!VisionMaster.getInstance().isLastDataValid()) return null;
        return planeryVisionDataToInnerHole(loc,angle);
    }

    public static double[] planeryVisionDataToInnerHole(){
        return planeryVisionDataToInnerHole(Chassis.getInstance().getAngle());
    }

    public static boolean isAngleGoodForShootingToInnerRads(double angle){
        return angle <= Math.toRadians(RobotMap.FieldData.anglePhysicalLimitForShootingToInnerDegrees) && angle <= Math.toRadians(RobotMap.Limbo2.angleForShootingToInnerDegrees);
    }

    public static boolean isAngleGoodForShootingToInnerRads(){
        return isAngleGoodForShootingToInnerRads(Chassis.getInstance().getAngle());
    }

}
