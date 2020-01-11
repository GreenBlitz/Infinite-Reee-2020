package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ArcadeDrive;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.RoborioEncoder;
import edu.greenblitz.gblib.gyroscope.IGyroscope;
import edu.greenblitz.gblib.gyroscope.PigeonGyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.greenblitz.motion.app.Localizer;
import org.greenblitz.motion.base.Position;

import java.awt.*;


public class Chassis implements Subsystem {
    private static Chassis instance;

    private VictorSPX leftVictor, rightVictor;
    private TalonSRX leftTalon, rightTalon;
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

        leftEncoder = new RoborioEncoder(
                RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_LEFT,
                RobotMap.BigRodika.Chassis.Encoder.LEFT_PORT_A,
                RobotMap.BigRodika.Chassis.Encoder.LEFT_PORT_B);
        rightEncoder = new RoborioEncoder(
                RobotMap.BigRodika.Chassis.Encoder.NORM_CONST_RIGHT,
                RobotMap.BigRodika.Chassis.Encoder.RIGHT_PORT_A,
                RobotMap.BigRodika.Chassis.Encoder.RIGHT_PORT_B);

        gyroscope = new PigeonGyro(new PigeonIMU(rightTalon));

    }

    public static Chassis getInstance() {
        if (instance == null) {
            instance = new Chassis();
            Chassis.getInstance().setDefaultCommand(
                    new ArcadeDrive(Chassis.getInstance(), OI.getInstance().getMainJoystick()));
        }
        return instance;
    }

    public void moveMotors(double left, double right){
        leftTalon.set(ControlMode.PercentOutput, left);
        rightTalon.set(ControlMode.PercentOutput, right);
    }

    public void toBrake(){
        leftTalon.setNeutralMode(NeutralMode.Brake);
        leftVictor.setNeutralMode(NeutralMode.Brake);
        rightTalon.setNeutralMode(NeutralMode.Brake);
        rightVictor.setNeutralMode(NeutralMode.Brake);
    }

    public void toCoast(){
        leftTalon.setNeutralMode(NeutralMode.Coast);
        leftVictor.setNeutralMode(NeutralMode.Coast);
        rightTalon.setNeutralMode(NeutralMode.Coast);
        rightVictor.setNeutralMode(NeutralMode.Coast);
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

    public double getAngularVelocityByWheels(double wheelDistance){
        return wheelDistance * (getLeftRate() - getRightRate());
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
        SmartDashboard.putNumber("Yaw", getAngle());
        SmartDashboard.putString("Location", Localizer.getInstance().getLocation().toString());
    }

}
