package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.RoborioEncoder;
import edu.greenblitz.gblib.gyroscope.IGyroscope;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.gblib.encoder.SparkEncoder;
import edu.greenblitz.gblib.gyroscope.NavxGyro;
import edu.greenblitz.gblib.gyroscope.PigeonGyro;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.greenblitz.motion.Localizer;
import org.greenblitz.motion.base.Position;



public class Chassis implements Subsystem {
    private static Chassis instance;

    private VictorSPX leftVictor, rightVictor;
    private TalonSRX leftTalon, rightTalon;   //chassis
//    private CANSparkMax rightLeader, rightFollower1, rightFollower2, leftLeader, leftFollower1, leftFollower2;
    private IEncoder leftEncoder, rightEncoder;
    private IGyroscope gyroscope;

    private Chassis() {

        leftVictor = new VictorSPX(RobotMap.BigRodika.Chassis.Motor.LEFT_VICTOR);
        rightVictor = new VictorSPX(RobotMap.BigRodika.Chassis.Motor.RIGHT_VICTOR);
        leftTalon = new TalonSRX(RobotMap.BigRodika.Chassis.Motor.LEFT_TALON);
        rightTalon = new TalonSRX(RobotMap.BigRodika.Chassis.Motor.RIGHT_TALON);

        rightVictor.setInverted(true);
        rightVictor.follow(rightTalon);
        leftVictor.follow(leftTalon);
        leftVictor.follow(leftTalon);   //chassis

//        rightLeader = new CANSparkMax(RobotMap.BigRodika.Chassis.Motor.RIGHT_LEADER, CANSparkMaxLowLevel.MotorType.kBrushless);
//        rightFollower1 = new CANSparkMax(RobotMap.BigRodika.Chassis.Motor.RIGHT_FOLLOWER_1, CANSparkMaxLowLevel.MotorType.kBrushless);
//        rightFollower2 = new CANSparkMax(RobotMap.BigRodika.Chassis.Motor.RIGHT_FOLLOWER_2, CANSparkMaxLowLevel.MotorType.kBrushless);
//        leftLeader = new CANSparkMax(RobotMap.BigRodika.Chassis.Motor.LEFT_LEADER, CANSparkMaxLowLevel.MotorType.kBrushless);
//        leftFollower1 = new CANSparkMax(RobotMap.BigRodika.Chassis.Motor.LEFT_FOLLOWER_1, CANSparkMaxLowLevel.MotorType.kBrushless);
//        leftFollower2 = new CANSparkMax(RobotMap.BigRodika.Chassis.Motor.LEFT_FOLLOWER_2, CANSparkMaxLowLevel.MotorType.kBrushless);   //big-haim
//
//        leftLeader.setInverted(true);
//        leftFollower1.follow(leftLeader);
//        leftFollower2.follow(leftLeader);
//        rightFollower1.follow(rightLeader);
//        rightFollower2.follow(rightLeader);



        leftEncoder = new RoborioEncoder(
                RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_LEFT,
                RobotMap.BigRodika.Chassis.Encoder.LEFT_PORT_A,
                RobotMap.BigRodika.Chassis.Encoder.LEFT_PORT_B);
        rightEncoder = new RoborioEncoder(
                RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_RIGHT,
                RobotMap.BigRodika.Chassis.Encoder.RIGHT_PORT_A,
                RobotMap.BigRodika.Chassis.Encoder.RIGHT_PORT_B);   //chassis
        leftEncoder.invert(true);
//        leftEncoder = new SparkEncoder(RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_SPARK, leftLeader);
//        leftEncoder.invert(true);
//        rightEncoder = new SparkEncoder(RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_SPARK, rightLeader);
//        rightEncoder.invert(true);

        gyroscope = new PigeonGyro(new PigeonIMU(rightTalon));   // chassis
//        gyroscope = new NavxGyro(new AHRS(SerialPort.Port.kUSB));  //big-haim
//        gyroscope.inverse();

    }

    public void changeGear(){
        leftEncoder.switchGear();
        rightEncoder.switchGear();
    }

    public static void init() {
        if (instance == null) {
            instance = new Chassis();
            instance.setDefaultCommand(
                    new ArcadeDrive(OI.getInstance().getMainJoystick())
            );
        }
    }

    public static Chassis getInstance() {
        return instance;
    }

    public void moveMotors(double left, double right){
        leftTalon.set(ControlMode.PercentOutput, left);
        rightTalon.set(ControlMode.PercentOutput, right);   //chassis
        SmartDashboard.putNumber("Left", left);
        SmartDashboard.putNumber("Right", right);
//        rightLeader.set(-right);
//        leftLeader.set(-left);   //big-haim
    }

    public void toBrake(){
        leftTalon.setNeutralMode(NeutralMode.Brake);
        leftVictor.setNeutralMode(NeutralMode.Brake);
        rightTalon.setNeutralMode(NeutralMode.Brake);
        rightVictor.setNeutralMode(NeutralMode.Brake);    //chassis
//        rightLeader.setIdleMode(CANSparkMax.IdleMode.kBrake);
//        rightFollower1.setIdleMode(CANSparkMax.IdleMode.kBrake);
//        rightFollower2.setIdleMode(CANSparkMax.IdleMode.kBrake);
//        leftLeader.setIdleMode(CANSparkMax.IdleMode.kBrake);
//        leftFollower1.setIdleMode(CANSparkMax.IdleMode.kBrake);
//        leftFollower2.setIdleMode(CANSparkMax.IdleMode.kBrake);   //big-haim
    }

    public void toCoast(){
        leftTalon.setNeutralMode(NeutralMode.Coast);
        leftVictor.setNeutralMode(NeutralMode.Coast);
        rightTalon.setNeutralMode(NeutralMode.Coast);
        rightVictor.setNeutralMode(NeutralMode.Coast);   //chassis
//        rightLeader.setIdleMode(CANSparkMax.IdleMode.kCoast);
//        rightFollower1.setIdleMode(CANSparkMax.IdleMode.kCoast);
//        rightFollower2.setIdleMode(CANSparkMax.IdleMode.kCoast);
//        leftLeader.setIdleMode(CANSparkMax.IdleMode.kCoast);
//        leftFollower1.setIdleMode(CANSparkMax.IdleMode.kCoast);
//        leftFollower2.setIdleMode(CANSparkMax.IdleMode.kCoast);   //big-haim
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        moveMotors(moveValue + rotateValue, moveValue - rotateValue);
    }

    public double getLeftMeters(){
        return leftEncoder.getNormalizedTicks();
    }

    public double getRightMeters(){
        return rightEncoder.getNormalizedTicks();
    }

    public double getLeftRate() {
        return leftEncoder.getNormalizedVelocity();
    }

    public double getRightRate(){
        return rightEncoder.getNormalizedVelocity();
    }

    public double getLinearVelocity(){
        return 0.5*(getLeftRate() + getRightRate());
    }

    public double getAngularVelocityByWheels(){
        return getWheelDistance() * (getLeftRate() - getRightRate());
    }

    public double getAngle(){
        return gyroscope.getNormalizedYaw();
    }

    public double getRawAngle() {
        return gyroscope.getRawYaw();
    }

    public double getAngularVelocityByGyro(){
        return gyroscope.getYawRate();
    }

    public void resetGyro(){
        gyroscope.reset();
    }

    public double getWheelDistance(){
        return RobotMap.BigRodika.Chassis.WHEEL_DIST;
    }

    public Position getLocation(){
        return Localizer.getInstance().getLocation();
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("Yaw", gyroscope.getRawYaw());
        SmartDashboard.putNumber("Left Dist", leftEncoder.getNormalizedTicks());
        SmartDashboard.putNumber("Right Dist", rightEncoder.getNormalizedTicks());
        SmartDashboard.putNumber("Left Vel", leftEncoder.getNormalizedVelocity());
        SmartDashboard.putNumber("Right Vel", rightEncoder.getNormalizedVelocity());
        SmartDashboard.putNumber("Ang vel", gyroscope.getYawRate());
        SmartDashboard.putString("Location", Localizer.getInstance().getLocation().toString());
        SmartDashboard.putNumber("angle", Chassis.getInstance().getAngle());
        SmartDashboard.putNumber("Chassis::rawEncoderRight", rightEncoder.getRawTicks());
        SmartDashboard.putNumber("Chassis::rawEncoderLeft", leftEncoder.getRawTicks());
        SmartDashboard.putString("Chassis::currentCommand", getCurrentCommand() == null? "null": getCurrentCommand().getName());
    }

    public void resetEncoders(){
        rightEncoder.reset();
        leftEncoder.reset();
    }

}
