package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Climber extends GBSubsystem {
    private static Climber instance;
    private WPI_TalonSRX hook;
    private CANSparkMax elevator;
    private DoubleSolenoid stopper;

    public Climber() {
        hook = new WPI_TalonSRX(RobotMap.Limbo2.Climber.Motor.HOOK);
        hook.setInverted(RobotMap.Limbo2.Climber.Motor.HOOK_REVERSE);
        elevator = new CANSparkMax(RobotMap.Limbo2.Climber.Motor.ELEVATOR, CANSparkMaxLowLevel.MotorType.kBrushless);
        elevator.setInverted(RobotMap.Limbo2.Climber.Motor.ELEVATOR_REVERSE);
        stopper = new DoubleSolenoid(RobotMap.Limbo2.Climber.Break.PCM,
                RobotMap.Limbo2.Climber.Break.FORWARD,
                RobotMap.Limbo2.Climber.Break.REVERSE);
    }

    public static void init() {
        instance = new Climber();
    }

    public static Climber getInstance() {
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
