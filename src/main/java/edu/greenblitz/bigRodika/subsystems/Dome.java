package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dome extends GBSubsystem {

    private static Dome instance;
    private final double POT_LOWER_LIMIT = 0.1,
            POT_HIGHER_LIMIT = 0.52;
    private static final double POWER_AT_LOWER_END = -0.05;
    private static final double MAX_VELOCITY = 0.2;
    private static final double MIN_VELOCITY = 0.004;
    private static final boolean SWITCH_ON = true;
    protected double lastPower = 0;
    protected double lastValue = Double.POSITIVE_INFINITY;
    protected long lastPotMeasureTime;
    private WPI_TalonSRX domeMotor;
    private IEncoder encoder;
    private DigitalInput limitSwitch;
    private double zeroValue = 0.35; //TODO: calibrate

    private Dome() {
        domeMotor = new WPI_TalonSRX(RobotMap.Limbo2.Dome.MOTOR_PORT);
        domeMotor.setInverted(RobotMap.Limbo2.Dome.IS_MOTOR_REVERSE);
        domeMotor.setNeutralMode(NeutralMode.Brake);
        encoder = new TalonEncoder(RobotMap.Limbo2.Dome.NORMALIZER, domeMotor);
        limitSwitch = DigitalInputMap.getInstance().getDigitalInput(
                RobotMap.Limbo2.Dome.LIMIT_SWITCH_PORT
        );
    }

    public static void init() {
        if (instance == null) {
            instance = new Dome();
            instance.register();
        }
    }

    public static Dome getInstance() {
        return instance;
    }

    public double getRawTicks() {
        return encoder.getRawTicks();
    }

    public double getEncoderValue() {
        return lastValue - zeroValue;
    }

    private void moveMotor(double power) {
        lastPower = power;
        domeMotor.set(power);
    }

    public void safeMove(double power) {
        if (switchTriggered() && power < 0) {
            moveMotor(0);
            return;
        }
        if (getEncoderValue() < POT_LOWER_LIMIT && power < 0) {
            moveMotor(Math.max(power, POWER_AT_LOWER_END));
        }
        if (getEncoderValue() > POT_HIGHER_LIMIT && power > 0) {
            moveMotor(0);
            return;
        }
        moveMotor(power);
    }

    public boolean switchTriggered() {
        return limitSwitch.get();
    }

    @Override
    public void periodic() {
        super.periodic();
        safeMove(lastPower);
        SmartDashboard.putNumber("DOME: RelaviveEncoderValue", getEncoderValue());
        SmartDashboard.putNumber("DOME: RawEncoderValues", getRawTicks());
        SmartDashboard.putBoolean("DOME: LimitSwitch", switchTriggered());
        putNumber("DOME: PotZero", zeroValue);

        double tempVal = getRawTicks();

        putNumber("DOME: DeltaPot", Math.abs(tempVal - lastValue));
        putNumber("DOME: Period Time", System.currentTimeMillis() - lastPotMeasureTime);
        if (lastValue == Double.POSITIVE_INFINITY ||
                (
                        Math.abs(tempVal - lastValue) < MAX_VELOCITY
                )

        ){
            lastValue = tempVal;
            lastPotMeasureTime = System.currentTimeMillis();
        }

        if (switchTriggered() && getCurrentCommand() != null && getCurrentCommand().getName().equals("ResetDome")
        && Math.abs(getRawTicks()) < 1.0) {
            zeroValue += getEncoderValue();
        }
    }
}
