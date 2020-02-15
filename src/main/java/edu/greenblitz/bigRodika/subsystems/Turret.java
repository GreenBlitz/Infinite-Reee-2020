package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANPIDController;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.greenblitz.motion.pid.PIDObject;

public class Turret extends GBSubsystem {
    private static Turret instance;

    private static final double MAX_TICKS = 10000;
    private static final double MIN_TICKS = -10000;

    public static void init(){
        if(instance == null) {
            instance = new Turret();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
    }

    public static Turret getInstance() {
        return instance;
    }

    private WPI_TalonSRX motor;
    private IEncoder encoder;
    private DigitalInput microSwitch;

    private Turret(){
        motor = new WPI_TalonSRX(RobotMap.Limbo2.Turret.MOTOR_PORT);
        encoder = new TalonEncoder(RobotMap.Limbo2.Turret.NORMALIZER, motor);
        microSwitch = new DigitalInput(RobotMap.Limbo2.Turret.SWITCH_PORT);
    }

    @Override
    public void periodic() {
        super.periodic();
        if (isSwitchPressed()){
            encoder.reset();
        }

        if (getTurretLocation() < MIN_TICKS && lastPower < 0) motor.set(0);
        if (getTurretLocation() > MAX_TICKS && lastPower > 0) motor.set(0);
    }

    private double lastPower = 0;

    public void moveTurret(double power){
        putNumber("Turret::power", power);
        if (getTurretLocation() < MIN_TICKS && power < 0) return;
        if (getTurretLocation() > MAX_TICKS && power > 0) return;
        motor.set(power);
        lastPower = power;
    }

    public double getSpeed(){
        return (encoder.getNormalizedVelocity());
    }

    public boolean isSwitchPressed(){
        return microSwitch.get();
    }

    public void moveTurretByPID(double target){
        if (inRange(MIN_TICKS, MAX_TICKS))
            motor.set(ControlMode.Velocity, target);
    }

    public double getTurretLocation(){
        return encoder.getNormalizedTicks();
    }

    public double getTurretSpeed() {
        return encoder.getNormalizedVelocity();
    }

    public double getAbsoluteTurretSpeed(){
        return Math.abs(getTurretSpeed());
    }

    public void configurePID(int pidIndex, PIDObject obj){
        motor.config_kP(pidIndex, obj.getKp());
        motor.config_kI(pidIndex, obj.getKi());
        motor.config_kD(pidIndex, obj.getKd());
        motor.config_kF(pidIndex, obj.getKf());
    }

    public void selectPIDLoop(int pidIndex){
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, pidIndex, 20);
    }

    public boolean inRange(double min, double max) {
        return encoder.getNormalizedTicks() < max && encoder.getNormalizedTicks() > min;
    }
}
