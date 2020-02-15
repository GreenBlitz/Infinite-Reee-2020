package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Climber extends GBSubsystem {
    private static Climber instance;
    private WPI_TalonSRX hook;
    private CANSparkMax elevator;
    private DoubleSolenoid stopper;

    public Climber() {
        hook = new WPI_TalonSRX(RobotMap.Limbo2.Climber.Motor.HOOK);
        elevator = new CANSparkMax(RobotMap.Limbo2.Climber.Motor.ELEVATOR, CANSparkMaxLowLevel.MotorType.kBrushless);
        stopper = new DoubleSolenoid(RobotMap.Limbo2.Climber.PCM, RobotMap.Limbo2.Climber.Solenoid.FORWARD, RobotMap.Limbo2.Climber.Solenoid.REVERSE);
    }

    public static void init(){
        instance = new Climber();
    }

    public static Climber getInstance(){
        return instance;
    }

    public void moveHook(double power) {
        hook.set(power);
    }

    public void moveElevator(double power) {
        elevator.set(power);
    }

    public void hold() {
        stopper.set(DoubleSolenoid.Value.kForward);
    }

    public void release() {
        stopper.set(DoubleSolenoid.Value.kReverse);
    }

    public boolean isStopped() {
        return stopper.get().equals(DoubleSolenoid.Value.kForward);
    }

    public void toggleStopper() {
        if (isStopped())
            release();
        else
            hold();
    }
}
