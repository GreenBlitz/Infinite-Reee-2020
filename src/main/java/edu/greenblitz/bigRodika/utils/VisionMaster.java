package edu.greenblitz.bigRodika.utils;

import edu.greenblitz.bigRodika.OI;
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
        POWER_CELL("send_power_cell"),
        HEXAGON("send_hexagon"),
        FEEDING_STATION("send_feeding_station"),
        ROULETTE("send_roulette");

        public final String rawAlgorithmName;

        Algorithm(String rawAlgorithmName) {
            this.rawAlgorithmName = rawAlgorithmName;
        }

        public String getRawName() {
            return rawAlgorithmName;
        }
    }

    public enum Error {
        NOT_ARRAY("vision value not double array"),
        UNEXPECTED_LENGTH("vision returned other than 4 values"),
        OK("");

        private final String msg;

        Error(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    private static VisionMaster instance;

    public static VisionMaster getInstance() {
        if (instance == null) {
            instance = new VisionMaster();
        }
        return instance;
    }

    private NetworkTable m_visionTable;
    private NetworkTableEntry m_algorithm;
    private NetworkTableEntry m_values;
    private NetworkTableEntry m_found;
    private Logger logger;
    private Error m_lastError = Error.OK;
    private double lastAngleToDrive = 0;

    public VisionMaster() {
        logger = LogManager.getLogger(getClass());
        m_visionTable = NetworkTableInstance.getDefault().getTable("vision");
        m_algorithm = m_visionTable.getEntry("algorithm");
        m_values = m_visionTable.getEntry("output");
        m_found = m_visionTable.getEntry("found");
    }

    public void setCurrentAlgorithm(Algorithm algo) {
        m_algorithm.setString(algo.getRawName());
    }

    public double[] getCurrentVisionData() {
        if (m_values.getType() != NetworkTableType.kDoubleArray) {
            if (m_lastError != Error.NOT_ARRAY) {
                logger.warn(Error.NOT_ARRAY);
                m_lastError = Error.NOT_ARRAY;
            }
            return null;
        }
        var ret = m_values.getValue().getDoubleArray();
        if (ret.length == 0 || ret.length % 4 != 0) {
            if (m_lastError != Error.UNEXPECTED_LENGTH) {
                logger.warn(Error.UNEXPECTED_LENGTH);
                m_lastError = Error.UNEXPECTED_LENGTH;
            }
            return null;
        }

        m_lastError = Error.OK;
        return ret;
    }

    public Error getLastError() {
        return m_lastError;
    }

    public StandardVisionData[] getStandardizedData() {
        double[] input = getCurrentVisionData();

        if (input == null) return new StandardVisionData[]
                {new StandardVisionData(new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN})};

        StandardVisionData[] ret = new StandardVisionData[input.length / 4];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new StandardVisionData(input[3 * i], input[3 * i + 1], input[3 * i + 2]);
        }
        return ret;
    }

    public double getPlaneryDistance(int ind) {
        return getStandardizedData()[ind].getPlanerDistance();
    }

    public double getRelativeAngle(int ind) {
        return getStandardizedData()[ind].getRelativeAngle();
    }

    public double getAngle(int ind) {
        return getStandardizedData()[ind].angle;
    }

    public double getPlaneryDistance() {
        return getPlaneryDistance(0);
    }

    public double getRelativeAngle() {
        return getRelativeAngle(0);
    }

    public double getAngle() {
        return getAngle(0);
    }

    public void getCurrentVisionData(double[] dest) {
        m_values.getDoubleArray(dest);
    }

    public boolean isDataValid() {
        return m_found.getBoolean(false) && m_lastError == Error.OK;
    }

    public void updateLastAngleToDrive(double offset) {
        SmartDashboard.putNumber("Vision::ChassisAngleAtUpdate", Chassis.getInstance().getAngle());
        SmartDashboard.putNumber("Vision::VisionAngleAtUpdate", getAngle());
        lastAngleToDrive = Math.toDegrees(Position.normalizeAngle(Math.toRadians(
                Chassis.getInstance().getAngle() + getAngle() + offset
        )));
    }

    public double getLastAngleToDrive() {
        return lastAngleToDrive;
    }

    public void update() {
        var current = getStandardizedData()[0];
        SmartDashboard.putString("Vision::focus", m_visionTable.getEntry("focus").getString(""));
        SmartDashboard.putString("Vision::raw data", current.toString());
        SmartDashboard.putNumber("Vision::planery distance", current.getPlanerDistance());
        SmartDashboard.putNumber("Vision::derived angle", current.angle);

    }
}