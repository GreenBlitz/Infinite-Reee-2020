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

public class Shooter extends GBSubsystem {

    private static Shooter instance;

    private CANSparkMax flywheel;
    private boolean preparedToShoot;

    private Shooter() {
        flywheel = new CANSparkMax(RobotMap.Limbo2.Shooter.PORT, CANSparkMaxLowLevel.MotorType.kBrushless);
        flywheel.setInverted(RobotMap.Limbo2.Shooter.IS_INVERTED);
        flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
        preparedToShoot = false;
//        flywheel.getEncoder().setVelocityConversionFactor(TICKS_PER_REVOLUTION);
//        encoder = new SparkEncoder(RobotMap.Limbo2.Shooter.NORMALIZER, flywheel);
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

    public boolean isPreparedToShoot() {
        return preparedToShoot;
    }

    public void setPreparedToShoot(boolean preparedToShoot) {
        this.preparedToShoot = preparedToShoot;
    }

    @Override
    public void periodic() {
        super.periodic();

        putNumber("Position", flywheel.getEncoder().getPosition());
        putNumber("Velocity", flywheel.getEncoder().getVelocity());
        putNumber("Output", flywheel.getAppliedOutput());
        putBoolean("ReadyToShoot", preparedToShoot);
    }

    public CANPIDController getPIDController() {
        return flywheel.getPIDController();
    }
}
