package edu.greenblitz.bigRodika.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.SparkEncoder;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Shooter implements Subsystem {
    private static Shooter instance;

    private CANSparkMax flywheel;
    private SparkEncoder encoder;

    private Shooter() {
        flywheel = new CANSparkMax(RobotMap.BigRodika.Shooter.PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        flywheel.setInverted(RobotMap.BigRodika.Shooter.IS_INVERTED);
        flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);

        encoder = new SparkEncoder(RobotMap.BigRodika.Shooter.NORMALIZER, flywheel);
    }

    public static void init(){
        if(instance == null) {
            instance = new Shooter();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
    }

    public static Shooter getInstance() {
        return instance;
    }

    public void setPower(double power) {
        this.flywheel.set(power);
    }

    public double getShooterSpeed() {
        return this.encoder.getNormalizedVelocity();
    }

    public double getAbsoluteShooterSpeed(){
        return Math.abs(getShooterSpeed());
    }

    @Override
    public void periodic() {
    }
}
