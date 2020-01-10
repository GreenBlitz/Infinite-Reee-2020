package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ArcadeDrive;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.RoborioEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.greenblitz.gblib.gyroscope.IGyroscope;
import edu.greenblitz.gblib.gyroscope.PigeonGyro;
import edu.greenblitz.utils.SmartRobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.greenblitz.motion.app.Localizer;

import java.util.Arrays;

public class Chassis implements Subsystem {
    private static Chassis instance;

    private VictorSPX leftVictor, rightVictor;
    private TalonSRX leftTalon, rightTalon;
    private IEncoder leftEncoder, rightEncoder;
    private IGyroscope gyroscope;
    private SmartRobotDrive robotDrive;

    private Chassis() {
        leftVictor = new VictorSPX(RobotMap.BigRodika.Chassis.Motor.LEFT_VICTOR);
        rightVictor = new VictorSPX(RobotMap.BigRodika.Chassis.Motor.RIGHT_VICTOR);
        leftTalon = new TalonSRX(RobotMap.BigRodika.Chassis.Motor.LEFT_TALON);
        rightTalon = new TalonSRX(RobotMap.BigRodika.Chassis.Motor.RIGHT_TALON);

        leftEncoder = new RoborioEncoder(
                RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_LEFT,
                RobotMap.BigRodika.Chassis.Encoder.LEFT_PORT_A,
                RobotMap.BigRodika.Chassis.Encoder.LEFT_PORT_B);
        rightEncoder = new RoborioEncoder(
                RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_RIGHT,
                RobotMap.BigRodika.Chassis.Encoder.RIGHT_PORT_A,
                RobotMap.BigRodika.Chassis.Encoder.RIGHT_PORT_B);

        gyroscope = new PigeonGyro(new PigeonIMU(rightTalon));

        robotDrive = new SmartRobotDrive(rightVictor, rightTalon, leftVictor, leftTalon);
        robotDrive.setInvetedMotor(SmartRobotDrive.TalonID.FRONT_RIGHT, true);

        setDefaultCommand(new ArcadeDrive(this, OI.getInstance().getMainJoystick()));
    }

    public static Chassis getInstance() {
        if (instance == null) {
            instance = new Chassis();
        }
        return instance;
    }

    public void moveMotors(double left, double right){
        robotDrive.tankDrive(left, right);
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        robotDrive.arcadeDrive(moveValue, rotateValue);
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

    public double getAngularVelocityByWheels(double wheelDistance){
        return wheelDistance * (getLeftRate() - getRightRate());
    }

    public double getAngle(){
        return gyroscope.getNormalizedYaw();
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

    @Override
    public void periodic(){
        SmartDashboard.putNumber("Yaw", getAngle());
        SmartDashboard.putString("Location", Localizer.getInstance().getLocation().toString());
    }

}
