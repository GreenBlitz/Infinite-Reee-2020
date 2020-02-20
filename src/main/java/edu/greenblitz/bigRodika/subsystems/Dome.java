package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dome extends GBSubsystem {

    private static Dome instance;
    private final double POT_LOWER_LIMIT = 0.1, // TODO calibrate
            POT_HIGHER_LIMIT = 0.5;
    private static final double POWER_AT_LOWER_END = -0.05;
    protected double lastPower = 0;
    private WPI_TalonSRX domeMotor;
    private Potentiometer potentiometer;
    private DigitalInput limitSwitch;
    private double zeroValue = 0;

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
        instance = new Dome();
    }

    public static Dome getInstance() {
        return instance;
    }

    public double getPotentiometerRaw() {
        return (RobotMap.Limbo2.Dome.IS_POTENTIOMETER_REVERSE
                ? 1 - potentiometer.get() : potentiometer.get());
    }

    public double getPotentiometerValue(){
        return getPotentiometerRaw() - zeroValue;
    }

    public void moveMotor(double power) {
        lastPower = power;
        domeMotor.set(power);
    }

    public void safeMove(double power) {
        if (switchTriggered() && power < 0){
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

    @Override
    public void periodic() {
        super.periodic();
        safeMove(lastPower);
        putNumber("Potentiometer", getPotentiometerValue());
        putBoolean("LimitSwitch", switchTriggered());
        putNumber("PotZero", zeroValue);

        if (switchTriggered()) {
            zeroValue += getPotentiometerValue();
        }
    }
}
