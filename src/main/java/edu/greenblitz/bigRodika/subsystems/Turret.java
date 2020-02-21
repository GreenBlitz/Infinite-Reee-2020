package edu.greenblitz.bigRodika.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.MotionUtils;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret extends GBSubsystem {
    private static final double MAX_TICKS = 5000;//16400
    private static final double MIN_TICKS = -15000;//-12400//-11000 todo :REALLY IMPORTANT FOR ROBOT NOT TO DIE.
    //asl @Peleg before changing
    private static Turret instance;
    private WPI_TalonSRX motor;
    private IEncoder encoder;
    private DigitalInput microSwitch;
    private double lastPower = 0;

    private Turret() {
        motor = new WPI_TalonSRX(RobotMap.Limbo2.Turret.MOTOR_PORT);
        motor.setInverted(RobotMap.Limbo2.Turret.IS_INVERTED);
        encoder = new TalonEncoder(RobotMap.Limbo2.Turret.NORMALIZER, motor);
        microSwitch = DigitalInputMap.getInstance().getDigitalInput(
                RobotMap.Limbo2.Turret.SWITCH_PORT
        );
    }

    public static void init() {
        if (instance == null) {
            instance = new Turret();
        }
    }

    public void resetEncoder(){
        encoder.reset();
    }

    public static Turret getInstance() {
        return instance;
    }

    @Override
    public void periodic() {
        super.periodic();
        if (isSwitchPressed()) {
            encoder.reset();
        }

        double[] simData = MotionUtils.getSimulatedVisionLocation(
                VisionMaster.getInstance().getVisionLocation().toDoubleArray());

        putNumber("Encoder", encoder.getRawTicks());
        putNumber("normEncoder", encoder.getNormalizedTicks());
        putNumber("Angle from front deg", Math.toDegrees(getNormAngleRads()));
        putBoolean("Switch", isSwitchPressed());
        putString("Simulated vision location",
                simData[0] + ", " + simData[1]);

        moveTurret(lastPower);
    }

    public void moveTurret(double power) {
        if (encoder.getRawTicks() < MIN_TICKS && power < 0) {
            motor.set(0);
            return;
        }
        if (encoder.getRawTicks() > MAX_TICKS && power > 0) {
            motor.set(0);
            return;
        }
        motor.set(power);
        lastPower = power;
    }

    public double getSpeed() {
        return (encoder.getNormalizedVelocity());
    }

    public boolean isSwitchPressed() {
        return !microSwitch.get(); // alexey i changed the code to match the new magnetic switch
    }

    public double getTurretLocation() {
        return encoder.getNormalizedTicks();
    }

    public double getNormAngleRads() {
        return 2 * Math.PI * (getTurretLocation() - RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD/RobotMap.Limbo2.Turret.NORMALIZER.getValue());
    }

    public double getTurretSpeed() {
        return encoder.getNormalizedVelocity();
    }

    public double getAbsoluteTurretSpeed() {
        return Math.abs(getTurretSpeed());
    }

}
