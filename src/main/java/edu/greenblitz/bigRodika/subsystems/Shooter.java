package edu.greenblitz.bigRodika.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.greenblitz.motion.pid.PIDObject;

public class Shooter implements Subsystem {
    private static Shooter instance;

    private static final double TICKS_PER_REVOLUTION = 1;

    private CANSparkMax flywheel;
//    private SparkEncoder encoder;

    private Shooter() {
        System.out.println("Created shooter!");
        flywheel = new CANSparkMax(RobotMap.BigRodika.Shooter.PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        flywheel.setInverted(RobotMap.BigRodika.Shooter.IS_INVERTED);
        flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
        flywheel.getEncoder().setVelocityConversionFactor(TICKS_PER_REVOLUTION);
//        encoder = new SparkEncoder(RobotMap.BigRodika.Shooter.NORMALIZER, flywheel);
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

    public void shoot(double power) {
        SmartDashboard.putNumber("Shooter::power", power);
        this.flywheel.set(power);
    }

    public void setSpeedByPID(double target){
        flywheel.getPIDController().setReference(target, ControlType.kVelocity);
    }

    public void setPIDConsts(PIDObject obj) {
        CANPIDController controller = flywheel.getPIDController();
        controller.setP(obj.getKp());
        controller.setI(obj.getKi());
        controller.setD(obj.getKd());
        controller.setFF(obj.getKf());
    }


    public double getShooterSpeed() {
        return flywheel.getEncoder().getVelocity();
    }

    public double getAbsoluteShooterSpeed(){
        return Math.abs(getShooterSpeed());
    }

    public void resetEncoder(){
        flywheel.getEncoder().setPosition(0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter::Position", flywheel.getEncoder().getPosition());
        SmartDashboard.putNumber("Shooter::Velocity", flywheel.getEncoder().getVelocity());
        SmartDashboard.putNumber("Shooter::output", flywheel.getAppliedOutput());
    }

}
