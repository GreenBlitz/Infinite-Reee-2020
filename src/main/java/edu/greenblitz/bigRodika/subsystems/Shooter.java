package edu.greenblitz.bigRodika.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.PIDShoot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Shooter implements Subsystem {
    private static Shooter instance;

    private CANSparkMax m_motor;
    private CANEncoder m_encoder;
    private CANPIDController m_pid;

    private Shooter() {
        this.m_motor = new CANSparkMax(RobotMap.BigRodika.Shooter.motorID, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.m_encoder = new CANEncoder(this.m_motor);
        this.m_pid = new CANPIDController(this.m_motor);
        this.m_pid.setP(RobotMap.BigRodika.Shooter.PID.P);
        this.m_pid.setI(RobotMap.BigRodika.Shooter.PID.I);
        this.m_pid.setD(RobotMap.BigRodika.Shooter.PID.D);
    }

    public static Shooter getInstance() {
        if(instance == null) {
            instance = new Shooter();
            CommandScheduler.getInstance().registerSubsystem(instance);
            instance.setDefaultCommand(
                    new PIDShoot(instance, 0)
            );

        }
        return instance;
    }

    public void setPower(double power) {
        power %= 1;
        this.m_motor.set(power);
    }

    public double getShooterSpeed() {
        return this.m_encoder.getVelocity();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter Speed:", this.m_encoder.getVelocity());
    }
}
