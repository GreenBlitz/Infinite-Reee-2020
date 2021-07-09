package edu.greenblitz.bigRodika.subsystems;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.Robot;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.MotionUtils;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.commands.turret.help.TurretToDefaultGood;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Turret extends GBSubsystem {
    public static final double MAX_TICKS = 1.22;
    public static final double MIN_TICKS = -0.083; // TODO: REALLY IMPORTANT FOR ROBOT NOT TO DIE
    //ask @Peleg before changing
    private static Turret instance;
    private WPI_TalonSRX motor;
    private IEncoder encoder;
    private DigitalInput microSwitch;
    private double lastPower = 0;

    private static boolean reset = false;

    public Command defaultCommand;

    private Turret() {
        motor = new WPI_TalonSRX(RobotMap.Limbo2.Turret.MOTOR_PORT);
        motor.setInverted(RobotMap.Limbo2.Turret.IS_INVERTED);
        motor.setNeutralMode(NeutralMode.Brake);
        encoder = new TalonEncoder(RobotMap.Limbo2.Turret.NORMALIZER, motor);
        microSwitch = DigitalInputMap.getInstance().getDigitalInput(
                RobotMap.Limbo2.Turret.SWITCH_PORT
        );


        motor.enableCurrentLimit(true);
        motor.configPeakCurrentLimit(RobotMap.Limbo2.Turret.PEAK_CURRENT_LIMIT);
        motor.configPeakCurrentDuration(RobotMap.Limbo2.Turret.PEAK_CURRENT_DURATION);

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

    public void moveTurretToSwitch() {
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
            System.err.println("HULL SENSOR WENT OFF");
        }

        SmartDashboard.putBoolean("Turret microSwitch", isSwitchPressed());
        SmartDashboard.putNumber("Turret Encoder Raw", encoder.getRawTicks());
        SmartDashboard.putNumber("TURRET Encoder NORM", encoder.getNormalizedTicks());
        SmartDashboard.putBoolean("MAX", encoder.getNormalizedTicks() > MAX_TICKS);
        SmartDashboard.putBoolean("MIN", encoder.getNormalizedTicks() < MIN_TICKS);

        moveTurret(lastPower);
    }

    public void moveTurret(double power) {
        if (encoder.getNormalizedTicks() < MIN_TICKS && power > 0) {
            motor.set(0);
            return;
        }
        if (encoder.getNormalizedTicks() > MAX_TICKS && power < 0) {
            motor.set(0);
            return;
        }
        motor.set(power);
        lastPower = power;
    }


    public void moveTurretToSwitch(double power) {
        if(!reset) {
            long tStart = System.currentTimeMillis();
            while(!this.isSwitchPressed() && System.currentTimeMillis() - tStart < 15000) {
                this.motor.set(power);
            }

            while(this.encoder.getRawTicks() > RobotMap.Limbo2.Turret.MIN_LIMIT &&
                    this.encoder.getRawTicks() < RobotMap.Limbo2.Turret.MAX_LIMIT) {
                this.motor.set(power);
            }
            this.moveTurret(0);
            reset = true;
        }
    }

    public void toBrake() {
        this.motor.setNeutralMode(NeutralMode.Brake);
    }

    public void toCoast() {
        this.motor.setNeutralMode(NeutralMode.Coast);
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

    public double getRawTicks() {
        return encoder.getRawTicks();
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
