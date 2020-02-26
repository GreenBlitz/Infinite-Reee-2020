package edu.greenblitz.bigRodika.utils;

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

    private static final long MAX_HANDSHAKE_TIME = 3000;

    private long lastPrintTime = 0;

    private static VisionMaster instance;
    private Algorithm currentAlgorithm;
    private GameState currentGameState;
    private ShifterState currentShifterState;
    private long lastHandShake;
    private Logger logger;

    private VisionMaster() {
        super();
        logger = LogManager.getLogger(getClass());
    }

    public static VisionMaster getInstance() {
        if (instance == null) {
            instance = new VisionMaster();
        }
        return instance;
    }

    private void setCurrentShifterState(ShifterState state) {
        this.currentShifterState = state;
    }

    private void setCurrentGameState(GameState state) {
        this.currentGameState = state;
    }

    public Algorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }

    private void setCurrentAlgorithm(Algorithm algo) {
        this.currentAlgorithm = algo;
        RS232Communication.getInstance().setAlgo(algo);
    }

    public boolean isLastDataValid() {
        return RS232Communication.getInstance().get() != null;
    }

    public double[] getCurrentRawVisionData() {
        VisionLocation data = RS232Communication.getInstance().get();
        if (data == null){
            return new double[] {Double.NaN, Double.NaN, Double.NaN};
        }
        return data.toDoubleArray();
    }

    public VisionLocation getVisionLocation() {
        VisionLocation loc = RS232Communication.getInstance().get();

        if (loc == null) return new VisionLocation(new double[]{Double.NaN, Double.NaN, Double.NaN});

        return loc;
    }

    @Override
    public void periodic() {
        VisionLocation current = getVisionLocation();

        if(System.currentTimeMillis() - lastPrintTime > 500){
            System.out.println(current.toString());
            lastPrintTime = System.currentTimeMillis();
        }
        if (currentAlgorithm != null) {
            SmartDashboard.putString("vision algo", currentAlgorithm.getRawName());
        }
        SmartDashboard.putString("vision raw data", current.toString());
        SmartDashboard.putNumber("vision planery distance", current.getPlaneDistance());
        SmartDashboard.putNumber("vision derived angle", current.getRelativeAngle());
        SmartDashboard.putBoolean("vision valid", isLastDataValid());
        SmartDashboard.putNumber("vision full distance", current.getFullDistance());
    }

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

        public void setAsCurrent() {
            VisionMaster.getInstance().setCurrentAlgorithm(this);
        }
    }

    public enum GameState {
        DISABLED("disabled"),
        AUTONOMOUS("auto"),
        TELEOP("teleop");


        public final String rawStateName;

        GameState(String rawStateName) {
            this.rawStateName = rawStateName;
        }

        public void setAsCurrent() {
            VisionMaster.getInstance().setCurrentGameState(this);
        }

        public String getRawName() {
            return rawStateName;
        }
    }

    public enum ShifterState {
        POWER("power"),
        SPEED("speed");


        public final String rawStateName;

        ShifterState(String rawStateName) {
            this.rawStateName = rawStateName;
        }

        public void setAsCurrent() {
            VisionMaster.getInstance().setCurrentShifterState(this);
        }

        public String getRawName() {
            return rawStateName;
        }
    }
}