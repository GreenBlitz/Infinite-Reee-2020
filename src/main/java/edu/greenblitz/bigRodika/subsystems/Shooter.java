package edu.greenblitz.bigRodika.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.greenblitz.motion.pid.PIDObject;

public class Shooter extends GBSubsystem {

    private static Shooter instance;

    // Leader is left, follower is right
    private CANSparkMax leader, follower;
    private boolean preparedToShoot;

    private Shooter() {
        leader = new CANSparkMax(RobotMap.Limbo2.Shooter.PORT_LEADER, CANSparkMaxLowLevel.MotorType.kBrushless);
        follower = new CANSparkMax(RobotMap.Limbo2.Shooter.PORT_FOLLOWER, CANSparkMaxLowLevel.MotorType.kBrushless);

        leader.setInverted(RobotMap.Limbo2.Shooter.IS_INVERTED_LEADER);

        follower.follow(leader, RobotMap.Limbo2.Shooter.IS_INVERTED_FOLLOWER);

        leader.setIdleMode(CANSparkMax.IdleMode.kCoast);
        follower.setIdleMode(CANSparkMax.IdleMode.kCoast);

        preparedToShoot = false;

        putNumber("testing_target", 0);
        putNumber("p", 0);
        putNumber("i", 0);
        putNumber("d", 0);
        putNumber("f", 0);

//        leader.getEncoder().setVelocityConversionFactor(TICKS_PER_REVOLUTION);
//        encoder = new SparkEncoder(RobotMap.Limbo2.Shooter.NORMALIZER, leader);
    }

    public static void init() {
        if (instance == null) {
            instance = new Shooter();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
    }

    public static Shooter getInstance() {
        return instance;
    }

    public void shoot(double power) {
        putNumber("power", power);
        this.leader.set(power);
    }

    public void setSpeedByPID(double target) {
        leader.getPIDController().setReference(target, ControlType.kVelocity);
    }

    public void setPIDConsts(PIDObject obj) {
        CANPIDController controller = leader.getPIDController();
        controller.setP(obj.getKp());
        controller.setI(obj.getKi());
        controller.setD(obj.getKd());
        controller.setFF(obj.getKf());
    }


    public double getShooterSpeed() {
        return leader.getEncoder().getVelocity();
    }

    public double getAbsoluteShooterSpeed() {
        return Math.abs(getShooterSpeed());
    }

    public void resetEncoder() {
        leader.getEncoder().setPosition(0);
    }

    public boolean isPreparedToShoot() {
        return preparedToShoot;
    }

    public void setPreparedToShoot(boolean preparedToShoot) {
        this.preparedToShoot = preparedToShoot;
    }

    @Override
    public void periodic() {

        putNumber("Position", leader.getEncoder().getPosition());
        putNumber("Velocity", leader.getEncoder().getVelocity());
        putNumber("Output", leader.getAppliedOutput());
        putBoolean("ReadyToShoot", preparedToShoot);

    }

    public CANPIDController getPIDController() {
        return leader.getPIDController();
    }
}
