package edu.greenblitz.bigRodika.utils;

import java.text.DecimalFormat;

public class StandardVisionData {
    public static final double DISTANCE_TOO_CLOSE = 4;

    /**
     * X coordinate of the target (horizontal distance)
     */
    public double x;

    /**
     * Y coordinate of the data (vertical distance)
     */
    public double y;

    /**
     * Z coordinate of the data (depth)
     */
    public double z;

    /**
     * The angle of the target, relative to the robot
     */
    public double angle;

    public StandardVisionData(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = calculateRelativeAngle(x, z);
    }

    public static double calculateRelativeAngle(double x, double z) {
        if(x / z == 90) {
            return Math.toRadians(361);
        }
        return Math.atan2(x, z);
    }

    public double getDistance() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * @return distance to target ignoring y component
     */
    public double getPlanerDistance() {
        return Math.hypot(x, z);
    }

    public double getRelativeAngle() {
        return Math.toDegrees(angle);
    }

    public StandardVisionData(double[] rawData) {
        this(rawData[0], rawData[1], rawData[2]);
    }

    public boolean isValid() {
        return Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z) && Double.isFinite(angle);
    }

    /**
     * Vision works in a certain range. If we are too far, the information will be invalid ({@link StandardVisionData#isValid()}).
     * If we are too close - the values are too big. Big enough that if they are the real range, vision shouldn't recognise them.
     * <p>And that's why we check if the data is too far to check if it is too close.</p>
     *
     * @return is the camera too close to target
     */
    public boolean isTooClose() {
        return getDistance() < DISTANCE_TOO_CLOSE;
    }

    @Override
    public String toString() {
        DecimalFormat fmt = new DecimalFormat("###.####");
        return "StandardVisionData{" +
                "x=" + fmt.format(x) +
                ", y=" + fmt.format(y) +
                ", z=" + fmt.format(z) +
                ", angle=" + fmt.format(angle) +
                '}';
    }
}