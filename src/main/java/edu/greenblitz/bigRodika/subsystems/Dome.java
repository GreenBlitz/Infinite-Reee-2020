package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Dome extends GBSubsystem {

    private static Dome instance;
    private WPI_TalonSRX domeMotor;
    private Potentiometer potentiometer;

    private final double POT_LOWER_LIMIT = 0.05,
                         POT_HIGHER_LIMIT = 0.95;

    private Dome(){

        domeMotor = new WPI_TalonSRX(RobotMap.Limbo2.Dome.MOTOR_PORT);
        domeMotor.setInverted(RobotMap.Limbo2.Dome.IS_MOTOR_REVERS);
        domeMotor.setNeutralMode(NeutralMode.Brake);
        potentiometer = new AnalogPotentiometer(RobotMap.Limbo2.Dome.POTENTIOMETER_PORT);

    }

    public static void init(){
        instance = new Dome();
    }

    public static Dome getInstance(){
        return instance;
    }

    public double getPotentiometerValue(){
        return RobotMap.Limbo2.Dome.IS_MOTOR_REVERS ? 1 - potentiometer.get() : potentiometer.get();
    }

    public void moveMotor(double power){
        domeMotor.set(power);
    }

    public void safeMove(double power) {
        if (getPotentiometerValue() < POT_LOWER_LIMIT && power < 0) return;
        if (getPotentiometerValue() > POT_HIGHER_LIMIT && power > 0) return;
        moveMotor(power);
    }

}
