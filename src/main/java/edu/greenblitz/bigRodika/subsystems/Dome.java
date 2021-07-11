package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dome extends GBSubsystem {

    private static Dome instance;
    private final double ENCODER_LOWER_LIMIT = 0.1,
            ENCODER_HIGHER_LIMIT = 2964; // soft limit for dome disconnection from gear
    private static final double POWER_AT_LOWER_END = -0.05;
    private static final double MAX_VELOCITY = 500; // arbitrary, find speed
    private static final double MIN_VELOCITY = 2; // arbitrary, find speed
    private static final boolean SWITCH_ON = true;
    protected double lastPower = 0;
    protected double lastValue = Double.POSITIVE_INFINITY;
    protected long lastPotMeasureTime;
    private WPI_TalonSRX domeMotor;
    private Encoder encoder;
    private DigitalInput limitSwitch;
    private double zeroValue = 0;

    private Dome() {
        domeMotor = new WPI_TalonSRX(RobotMap.Limbo2.Dome.MOTOR_PORT);
        domeMotor.setInverted(RobotMap.Limbo2.Dome.IS_MOTOR_REVERSE);
        domeMotor.setNeutralMode(NeutralMode.Brake);
        DigitalInput channelA = DigitalInputMap.getInstance().getDigitalInput(RobotMap.Limbo2.Dome.ENCODER_PORT_A);
        DigitalInput channelB = DigitalInputMap.getInstance().getDigitalInput(RobotMap.Limbo2.Dome.ENCODER_PORT_B);
        encoder = new Encoder(channelA, channelB);
        encoder.reset();
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
        return encoder.getRaw();
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
        if (getEncoderValue() < ENCODER_LOWER_LIMIT && power < 0) {
            moveMotor(Math.max(power, POWER_AT_LOWER_END));
        }
        if (getEncoderValue() > ENCODER_HIGHER_LIMIT && power > 0) {
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
        SmartDashboard.putNumber("DOME: RelativeEncoderValue", getEncoderValue());
        SmartDashboard.putNumber("DOME: RawEncoderValues", getRawTicks());
        SmartDashboard.putBoolean("DOME: LimitSwitch", switchTriggered());
        SmartDashboard.putNumber("DOME: ZeroValue", zeroValue);


        double tempVal = getRawTicks();

        putNumber("DOME: DeltaPot", Math.abs(tempVal - lastValue));
        putNumber("DOME: Period Time", System.currentTimeMillis() - lastPotMeasureTime);
        if (lastValue == Double.POSITIVE_INFINITY || (Math.abs(tempVal - lastValue) < MAX_VELOCITY)) {
            lastValue = tempVal;
            lastPotMeasureTime = System.currentTimeMillis();
        }

        if (switchTriggered() && getCurrentCommand() != null && getCurrentCommand().getName().equals("ResetDome")) {
            zeroValue = getRawTicks();
        }
    }
}
