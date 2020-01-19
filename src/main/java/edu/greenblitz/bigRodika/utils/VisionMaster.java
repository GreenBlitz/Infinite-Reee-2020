package edu.greenblitz.bigRodika.utils;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenblitz.motion.base.Position;

/**
 * able to handle more that one at a time
 */
public class VisionMaster {
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
        algorithm.setString(algo.getRawName());
    }

    public boolean isLastDataValid(){
        return found.getBoolean(false);
    }

    public double[] getCurrentRawVisionData() {
        if (output.getType() != NetworkTableType.kDoubleArray) {
            logger.warn("Vision sent data that isn't a double array");
            return null;
        }
        return output.getValue().getDoubleArray();
    }

    public VisionLocation getVisionLocation() {
        double[] input = getCurrentRawVisionData();

        if (input == null) return new VisionLocation(new double[]{Double.NaN, Double.NaN, Double.NaN});

        return new VisionLocation(input);
    }

    public void update() {
        VisionLocation current = getVisionLocation();
        SmartDashboard.putString("Vision::algorithm", algorithm.getString("Not Existing"));
        SmartDashboard.putString("Vision::raw data", current.toString());
        SmartDashboard.putNumber("Vision::planery distance", current.getPlaneDistance());
        SmartDashboard.putNumber("Vision::derived angle", current.getRelativeAngle());
        SmartDashboard.putBoolean("Vision::valid", isLastDataValid());

    }
}