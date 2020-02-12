package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.greenblitz.motion.pid.PIDObject;

/**
 * The pusher is the motor in the funnel that pulls the balls from the outer parts of the funnel to the
 * inner parts of the funnel (This motor moves 3 "PVC" pipes and has no encoders).
 * The inserter inserts the balls into the shooter. It powers a flywheel-like thingy and has an encoder.
 */
public class Funnel {

    private static Funnel instance;
    private Inserter inserter;
    private Pusher pusher;

    public class Inserter extends GBSubsystem {
        private WPI_TalonSRX inserter;
        private TalonEncoder inserterEncoder;

        private Funnel parent;

        public Funnel getFunnel() {
            return parent;
        }

        private Inserter(Funnel parent){
            this.parent = parent;
            inserter = new WPI_TalonSRX(RobotMap.Limbo2.Funnel.Motors.INSERTER_PORT);
            inserterEncoder = new TalonEncoder(RobotMap.Limbo2.Funnel.Encoder.NORMALIZER, inserter);
        }

        @Override
        public void periodic() {

            super.periodic();

        }

    }

    public class Pusher extends GBSubsystem{
        private WPI_TalonSRX pusher;

        private Funnel parent;

        public Funnel getFunnel() {
            return parent;
        }

        private Pusher(Funnel parent){
            this.parent = parent;
            pusher = new WPI_TalonSRX(RobotMap.Limbo2.Funnel.Motors.PUSHER_PORT);
        }

        @Override
        public void periodic() {

            super.periodic();

        }
    }

    private Funnel() {
        inserter = new Inserter(this);
        pusher = new Pusher(this);
    }

    public static void init(){
        if (instance == null) {
            instance = new Funnel();
            CommandScheduler.getInstance().registerSubsystem(instance.inserter);
            CommandScheduler.getInstance().registerSubsystem(instance.pusher);
        }
    }

    public static Funnel getInstance() {
        return instance;
    }

    public void movePusher(double power) {
        pusher.pusher.set(power);
    }

    public void moveInserter(double power) {
        inserter.inserter.set(power);
    }

    public void moveInserterByPID(double target){
        inserter.inserter.set(ControlMode.Velocity, target);
    }

    public double getInserterSpeed() {
        return inserter.inserterEncoder.getNormalizedVelocity();
    }

    public double getAbsoluteInserterSpeed(){
        return Math.abs(getInserterSpeed());
    }

    public void configurePID(int pidIndex, PIDObject obj){
        inserter.inserter.config_kP(pidIndex, obj.getKp(), 20);
        inserter.inserter.config_kI(pidIndex, obj.getKi(), 20);
        inserter.inserter.config_kD(pidIndex, obj.getKd(), 20);
        inserter.inserter.config_kF(pidIndex, obj.getKf(), 20);
    }

    public void selectPIDLoop(int pidIndex){
        inserter.inserter.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, pidIndex, 20);
    }

    public Inserter getInserter(){
        return inserter;
    }

    public Pusher getPusher() {
        return pusher;
    }

}
