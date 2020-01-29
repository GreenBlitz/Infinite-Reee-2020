package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Funnel implements Subsystem {
    private static Funnel instance;

    /**
     * The pusher is the motor in the funnel that pulls the balls from the outer parts of the funnel to the
     * inner parts of the funnel (This motor moves 3 "PVC" pipes and has no encoders).
     * The inserter inserts the motors into the shooter. It powers a flywheel-like thingy and has an encoder.
     */
    private WPI_TalonSRX pusher, inserter;
    private TalonEncoder inserterEncoder;

    private Funnel() {
        pusher = new WPI_TalonSRX(RobotMap.BigRodika.Funnel.Motors.PUSHER_PORT);
        inserter = new WPI_TalonSRX(RobotMap.BigRodika.Funnel.Motors.INSERTER_PORT);
        inserterEncoder = new TalonEncoder(RobotMap.BigRodika.Funnel.Encoder.NORMALIZER, inserter);
    }

    public static void init(){
        if (instance == null) {
            instance = new Funnel();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
    }

    public static Funnel getInstance() {
        return instance;
    }

    public void movePusher(double power) {
        pusher.set(power);
    }

    public void moveInserter(double power) {
        inserter.set(power);
    }

    public double getInserterSpeed() {
        return inserterEncoder.getNormalizedVelocity();
    }

    public double getAbsoluteInserterSpeed(){
        return Math.abs(getInserterSpeed());
    }

    @Override
    public void periodic() {
    }
}
