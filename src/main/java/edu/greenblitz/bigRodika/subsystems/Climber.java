package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.greenblitz.gblib.gears.GearDependentValue;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends GBSubsystem {
    private static Climber instance;
    private WPI_TalonSRX elevator;
    private CANSparkMax hook;
    private CANEncoder hookEncoder;
    private TalonEncoder elevatorEncoder;
    private Servo stopper;

    public Climber() {
        elevator = new WPI_TalonSRX(RobotMap.Limbo2.Climber.Motor.ELEVATOR);
        elevatorEncoder = new TalonEncoder(new GearDependentValue<>(RobotMap.Limbo2.Climber.Motor.ELEVATOR_RATIO,RobotMap.Limbo2.Climber.Motor.ELEVATOR_RATIO), elevator);
        elevator.setInverted(RobotMap.Limbo2.Climber.Motor.HOOK_REVERSE);
        hook = new CANSparkMax(RobotMap.Limbo2.Climber.Motor.HOOK, CANSparkMaxLowLevel.MotorType.kBrushless);
        hookEncoder = new CANEncoder(hook);
        hook.setInverted(RobotMap.Limbo2.Climber.Motor.HOOK_REVERSE);
        stopper = new Servo(RobotMap.Limbo2.Climber.Break.SERVO_PORT);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Servo VAlue:", stopper.get());
        SmartDashboard.putNumber("Hook ticks:", this.getHookTicks());
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

    public double getHookTicks() {
        return hookEncoder.getPosition();
    }

    public double getElevatorPosition() {
        return elevatorEncoder.getNormalizedTicks();
    }

    public void hold() {
        stopper.set(RobotMap.Limbo2.Climber.Break.HOLD_VALUE);
    }

    public void release() {
        stopper.set(RobotMap.Limbo2.Climber.Break.RELEASE_VALUE);
    }

    private boolean isStopped() {
        return stopper.get() == RobotMap.Limbo2.Climber.Break.HOLD_VALUE;
    }

    public void toggleStopper() {
        if (isStopped())
            release();
        else
            hold();
    }
}