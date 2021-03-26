package edu.greenblitz.bigRodika.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.interpolation.Dataset;
import org.greenblitz.motion.pid.PIDObject;

public class Shooter extends GBSubsystem {

    private static Shooter instance;

    // Leader is left, follower is right
    private CANSparkMax leader, follower;
    private Dataset rpmToPowerMap;
    private boolean preparedToShoot;

    private Shooter() {
        leader = new CANSparkMax(RobotMap.Limbo2.Shooter.PORT_LEADER, CANSparkMaxLowLevel.MotorType.kBrushless);
        follower = new CANSparkMax(RobotMap.Limbo2.Shooter.PORT_FOLLOWER, CANSparkMaxLowLevel.MotorType.kBrushless);

        leader.setInverted(RobotMap.Limbo2.Shooter.IS_INVERTED_LEADER);

        follower.follow(leader, RobotMap.Limbo2.Shooter.IS_INVERTED_FOLLOWER);

        leader.setIdleMode(CANSparkMax.IdleMode.kCoast);
        follower.setIdleMode(CANSparkMax.IdleMode.kCoast);

        leader.setSmartCurrentLimit(40);
        follower.setSmartCurrentLimit(40);


        /*Orel & Itgil's measured table
        0 - 0
        0.2 - 790 (720)
        0.4 - 1950 (1860)
        0.6 - 2970 (2920)
        0.8 - 3210 (3500)
        */
        preparedToShoot = false;
        rpmToPowerMap = new Dataset(2);
        rpmToPowerMap.addDatapoint(0, new double[]{0});
        rpmToPowerMap.addDatapoint(790, new double[]{0.2});
        rpmToPowerMap.addDatapoint(1950, new double[]{0.4});
        rpmToPowerMap.addDatapoint(2970, new double[]{0.6});
        rpmToPowerMap.addDatapoint(3210, new double[]{0.8});
        // No fucking idea how much is 1.0, but 0.8 is already very fucking scary
//        rpmToPowerMap.addDatapoint(5500, new double[]{1.0});

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
        }
    }

    public static Shooter getInstance() {
        return instance;
    }

    public void shoot(double power) {
        putNumber("power", power);
        this.leader.set(power);
    }

    public double getDesiredPower(double rpm){
        return rpmToPowerMap.linearlyInterpolate(rpm)[0];
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
        SmartDashboard.putNumber("Velocity", leader.getEncoder().getVelocity());
        putNumber("Output", leader.getAppliedOutput());
        putBoolean("ReadyToShoot", preparedToShoot);

    }

    public CANPIDController getPIDController() {
        return leader.getPIDController();
    }
}
