package edu.greenblitz.bigRodika.utils;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.GBSubsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * able to handle more that one at a time
 */
public class VisionMaster extends GBSubsystem {
    public enum Algorithm {
        POWER_CELLS("power_cells"),
        HEXAGON("hexagon"),
        FEEDING_STATION("feeding_station"),
        ROULETTE("roulette");

        public final String rawAlgorithmName;

        Algorithm(String rawAlgorithmName) {
            this.rawAlgorithmName = rawAlgorithmName;
        }

        public String getRawName() {
            return rawAlgorithmName;
        }

        public void setAsCurrent(){
            VisionMaster.getInstance().setCurrentAlgorithm(this);
        }
    }

    private static VisionMaster instance;

    public static VisionMaster getInstance() {
        if (instance == null) {
            instance = new VisionMaster();
        }
        return instance;
    }

    private Algorithm currentAlgorithm;

    private NetworkTable visionTable;
    private NetworkTableEntry algorithm;
    private NetworkTableEntry output;
    private NetworkTableEntry found;
    private Logger logger;

    private VisionMaster() {
        logger = LogManager.getLogger(getClass());
        visionTable = NetworkTableInstance.getDefault().getTable("vision"); // table

        algorithm = visionTable.getEntry("algorithm"); // our output, tells the rpi what algorithm to run
        output = visionTable.getEntry("output"); // our input, the output of the vision (usually a double array of size 3)
        found = visionTable.getEntry("found"); // our input, boolean indicating if the last sent data was valid
    }

    private void setCurrentAlgorithm(Algorithm algo) {
        this.currentAlgorithm = algo;
        algorithm.setString(algo.getRawName());
    }

    public Algorithm getCurrentAlgorithm(){
        return currentAlgorithm;
    }

    public boolean isLastDataValid(){
        return found.getBoolean(false);
    }

    public double[] getCurrentRawVisionData() {
        if (output.getType() != NetworkTableType.kDoubleArray) {
            putString("Error", "Vision sent data that isn't a double array");
            return null;
        }
        putString("Error", "None");
        return output.getValue().getDoubleArray();
    }

    public VisionLocation getVisionLocation() {
        double[] input = getCurrentRawVisionData();

        if (input == null) return new VisionLocation(new double[]{Double.NaN, Double.NaN, Double.NaN});

        // TODO Temp because vision is dumb
        if (algorithm.getString("Bruh").equals("hexagon")) {
            double full_dist_squared = input[0] * input[0] + input[1] * input[1] + input[2] * input[2];
            input[1] = RobotMap.Limbo2.Chassis.MotionData.HEXAGON_CAMERA_H_DIFF;
            input[2] = Math.sqrt(full_dist_squared
                    - Math.pow(input[0], 2) - Math.pow(input[1], 2));
        }
        // Bruh moment

        return new VisionLocation(input);
    }

    @Override
    public void periodic() {
        VisionLocation current = getVisionLocation();
        putString("algorithm", algorithm.getString("Not Existing"));
        putString("raw data", current.toString());
        putNumber("planery distance", current.getPlaneDistance());
        putNumber("derived angle", current.getRelativeAngle());
        putBoolean("valid", isLastDataValid());
        putNumber("full distance", current.getFullDistance());
    }
}