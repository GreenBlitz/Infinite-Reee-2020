package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Dome extends GBSubsystem {

    private static Dome instance;
    private final double POT_LOWER_LIMIT = 0.1, // TODO calibrate
            POT_HIGHER_LIMIT = 0.5;
    private static final double POWER_AT_LOWER_END = -0.05;
    private static final double MAX_VELOCITY = 0.2;
    protected double lastPower = 0;
    protected double lastPotValue = Double.POSITIVE_INFINITY;
    private WPI_TalonSRX domeMotor;
    private Potentiometer potentiometer;
    private DigitalInput limitSwitch;
    private double zeroValue = 0.31;

    private Dome() {
        domeMotor = new WPI_TalonSRX(RobotMap.Limbo2.Dome.MOTOR_PORT);
        domeMotor.setInverted(RobotMap.Limbo2.Dome.IS_MOTOR_REVERSE);
        domeMotor.setNeutralMode(NeutralMode.Brake);
        potentiometer = new AnalogPotentiometer(RobotMap.Limbo2.Dome.POTENTIOMETER_PORT);
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

    public double getPotentiometerRaw() {
        return (RobotMap.Limbo2.Dome.IS_POTENTIOMETER_REVERSE
                ? 1 - potentiometer.get() : potentiometer.get());
    }

    public double getPotentiometerValue() {
        return lastPotValue - zeroValue;
    }

    public void moveMotor(double power) {
        lastPower = power;
        domeMotor.set(power);
    }

    public void safeMove(double power) {
        if (switchTriggered() && power < 0) {
            moveMotor(0);
            return;
        }
        if (getPotentiometerValue() < POT_LOWER_LIMIT && power < 0) {
            moveMotor(Math.max(power, POWER_AT_LOWER_END));
        }
        if (getPotentiometerValue() > POT_HIGHER_LIMIT && power > 0) {
            moveMotor(0);
            return;
        }
        moveMotor(power);
    }

    public boolean switchTriggered() {
        return limitSwitch.get();
    }

    private double tempVal;
    @Override
    public void periodic() {
        super.periodic();
        safeMove(lastPower);
        putNumber("Potentiometer", getPotentiometerValue());
        putBoolean("LimitSwitch", switchTriggered());
        putNumber("PotZero", zeroValue);

        tempVal = getPotentiometerRaw();
        if (lastPotValue == Double.POSITIVE_INFINITY || Math.abs(tempVal - lastPotValue) < MAX_VELOCITY){
            lastPotValue = tempVal;
        }

        if (switchTriggered() && getCurrentCommand() != null && getCurrentCommand().getName().equals("ResetDome")) {
//            if (getPotentiometerValue() < 0.05) {
//                zeroValue += getPotentiometerValue();
//            } else if (getCurrentCommand() != null && getCurrentCommand().getName().equals("ResetDome")) {
//                zeroValue += getPotentiometerValue();
//            }
            zeroValue += getPotentiometerValue();
        }
    }
}
