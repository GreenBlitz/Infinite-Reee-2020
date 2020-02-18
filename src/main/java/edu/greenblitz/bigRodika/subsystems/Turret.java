package edu.greenblitz.bigRodika.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Turret extends GBSubsystem {
    private static final double MAX_TICKS = 10000;
    private static final double MIN_TICKS = 0;
    private static Turret instance;
    private WPI_TalonSRX motor;
    private IEncoder encoder;
    private DigitalInput microSwitch;
    private double lastPower = 0;
    private Turret() {
        motor = new WPI_TalonSRX(RobotMap.Limbo2.Turret.MOTOR_PORT);
        motor.setInverted(RobotMap.Limbo2.Turret.IS_INVERTED);
        encoder = new TalonEncoder(RobotMap.Limbo2.Turret.NORMALIZER, motor);
        microSwitch = new DigitalInput(RobotMap.Limbo2.Turret.SWITCH_PORT);
    }

    public static void init() {
        if (instance == null) {
            instance = new Turret();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
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

        moveTurret(lastPower);
    }

    public void moveTurret(double power) {
        if (getTurretLocation() < MIN_TICKS && power < 0) {
            motor.set(0);
            return;
        }
        if (getTurretLocation() > MAX_TICKS && power > 0) {
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
        return microSwitch.get();
    }

    public double getTurretLocation() {
        return encoder.getNormalizedTicks();
    }

    public double getAngleRads(){
        return 2* Math.PI * getTurretLocation()/RobotMap.Limbo2.Turret.TICKSPERROUND;
    }

    public double getTurretSpeed() {
        return encoder.getNormalizedVelocity();
    }

    public double getAbsoluteTurretSpeed() {
        return Math.abs(getTurretSpeed());
    }


}
