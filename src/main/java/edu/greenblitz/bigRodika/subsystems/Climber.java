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

public class Climber extends GBSubsystem {
    private static Climber instance;
    private WPI_TalonSRX hook;
    private CANSparkMax elevator;
    private TalonEncoder hookEncoder;
    private CANEncoder elevatorEncoder;
    private Servo stopper;

    public Climber() {
        hook = new WPI_TalonSRX(RobotMap.Limbo2.Climber.Motor.HOOK);
        hookEncoder = new TalonEncoder(new GearDependentValue<>(RobotMap.Limbo2.Climber.Motor.HOOK_RATIO,RobotMap.Limbo2.Climber.Motor.HOOK_RATIO), hook);
        hook.setInverted(RobotMap.Limbo2.Climber.Motor.HOOK_REVERSE);
        elevator = new CANSparkMax(RobotMap.Limbo2.Climber.Motor.ELEVATOR, CANSparkMaxLowLevel.MotorType.kBrushless);
        elevatorEncoder = new CANEncoder(elevator);
        elevator.setInverted(RobotMap.Limbo2.Climber.Motor.ELEVATOR_REVERSE);

        stopper = new Servo(RobotMap.Limbo2.Climber.Break.SERVO_PORT);
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
        return hookEncoder.getNormalizedTicks();
    }

    public double getElevatorPosition() {
        return elevatorEncoder.getPosition();
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