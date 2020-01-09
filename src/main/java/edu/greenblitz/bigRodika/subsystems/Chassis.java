package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.ArcadeDrive;
import edu.greenblitz.utils.SmartRobotDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Chassis implements Subsystem {
    private static Chassis instance;

    private VictorSPX leftVictor, rightVictor;
    private TalonSRX leftTalon, rightTalon;
    private SmartRobotDrive robotDrive;

    private Chassis() {
        leftVictor = new VictorSPX(RobotMap.BigRodika.Chassis.Motor.LEFT_VICTOR);
        rightVictor = new VictorSPX(RobotMap.BigRodika.Chassis.Motor.RIGHT_VICTOR);
        leftTalon = new TalonSRX(RobotMap.BigRodika.Chassis.Motor.LEFT_TALON);
        rightTalon = new TalonSRX(RobotMap.BigRodika.Chassis.Motor.RIGHT_TALON);

        robotDrive = new SmartRobotDrive(rightVictor, rightTalon, leftVictor, leftTalon);
        robotDrive.setInvetedMotor(SmartRobotDrive.TalonID.FRONT_RIGHT, true);
    }

    public static Chassis getInstance() {
        if (instance == null) {
            instance = new Chassis();
        }
        return instance;
    }

    @Override
    public Command getDefaultCommand() {
        return new ArcadeDrive(OI.getInstance().getMainJoystick());
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        robotDrive.arcadeDrive(moveValue, rotateValue);
    }
}
