package edu.greenblitz.bigRodika.subsystems;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.MotionUtils;
import edu.greenblitz.bigRodika.commands.turret.help.TurretToDefaultGood;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Turret extends GBSubsystem {
    private static final double MAX_TICKS = 19500;
    private static final double MIN_TICKS = -1000; // TODO: REALLY IMPORTANT FOR ROBOT NOT TO DIE
    //ask @Peleg before changing
    private static Turret instance;
    private WPI_TalonSRX motor;
    private IEncoder encoder;
    private DigitalInput microSwitch;
    private double lastPower = 0;

    public Command defaultCommand;

    private Turret() {
        motor = new WPI_TalonSRX(RobotMap.Limbo2.Turret.MOTOR_PORT);
        motor.setInverted(RobotMap.Limbo2.Turret.IS_INVERTED);
        motor.setNeutralMode(NeutralMode.Brake);
        encoder = new TalonEncoder(RobotMap.Limbo2.Turret.NORMALIZER, motor);
        microSwitch = DigitalInputMap.getInstance().getDigitalInput(
                RobotMap.Limbo2.Turret.SWITCH_PORT
        );
    }

    public static void init() {
        if (instance == null) {
            instance = new Turret();
        }
    }

    public static void setDefaultCommand() {
        instance.defaultCommand = new TurretToDefaultGood(
                new JoystickButton[]{
                        OI.getInstance().getSideStick().R1,
                        OI.getInstance().getSideStick().START,
                        OI.getInstance().getMainJoystick().R1,
                        OI.getInstance().getMainJoystick().START},
                Math.toRadians(10.0),
                Math.toRadians(20.0), Math.toRadians(5.0),
                0.4, 0.02,
                0.02);
    }

    public void resetEncoder() {
        encoder.reset();
    }

    public void resetEncoder(int value) {
        motor.setSelectedSensorPosition(value);
    }


    public static Turret getInstance() {
        return instance;
    }

    @Override
    public void periodic() {
        super.periodic();
        if (isSwitchPressed()) {
            encoder.reset();
        }
        SmartDashboard.putBoolean("Turret microSwitch", isSwitchPressed());
        SmartDashboard.putNumber("Turret Encoder", encoder.getRawTicks());

        moveTurret(lastPower);
    }

    public void moveTurret(double power) {
        if (encoder.getRawTicks() < MIN_TICKS && power > 0) {
            motor.set(0);
            return;
        }
        if (encoder.getRawTicks() > MAX_TICKS && power < 0) {
            motor.set(0);
            return;
        }
        motor.set(power);
        lastPower = power;
    }

    public double getSpeed() {
        return (encoder.getNormalizedVelocity());
    }

    public boolean isSwitchPressed() {
        return !microSwitch.get();
    }

    public double getTurretLocation() {
        return encoder.getNormalizedTicks();
    }

    public double getNormAngleRads() {
        return 2 * Math.PI * (getTurretLocation() - RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD / RobotMap.Limbo2.Turret.NORMALIZER.getValue());
    }

    public double getNormAngleDegs() {
        return Math.toDegrees(getNormAngleRads());
    }

    public double getTurretSpeed() {
        return encoder.getNormalizedVelocity();
    }

    public double getAbsoluteTurretSpeed() {
        return Math.abs(getTurretSpeed());
    }

}
