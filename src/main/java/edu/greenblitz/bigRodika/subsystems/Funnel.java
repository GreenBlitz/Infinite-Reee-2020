package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The pusher is the motor in the funnel that pulls the balls from the outer parts of the funnel to the
 * inner parts of the funnel (This motor moves 3 "PVC" pipes and has no encoders).
 * The inserter inserts the balls into the shooter. It powers a flywheel-like thingy and has an encoder.
 */
public class Funnel {

    private static Funnel instance;
    private Inserter inserter;
    private Pusher pusher;

    private Funnel() {
        inserter = new Inserter(this);
        pusher = new Pusher(this);
    }

    public static void init() {
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
        movePusher(power, power);
    }

    public void movePusher(double left, double right) {
        pusher.pusher_right.set(left);
        pusher.pusher_left.set(right);
    }

    public void moveInserter(double power) {
        inserter.inserter.set(power);
    }

    public Inserter getInserter() {
        return inserter;
    }

    public Pusher getPusher() {
        return pusher;
    }

    public class Inserter extends GBSubsystem {
        private WPI_TalonSRX inserter;

        private Funnel parent;

        private Inserter(Funnel parent) {
            this.parent = parent;
            inserter = new WPI_TalonSRX(RobotMap.Limbo2.Funnel.Motors.INSERTER_PORT);
            inserter.setInverted(RobotMap.Limbo2.Funnel.Motors.INSERTER_REVERSED);
        }

        public Funnel getFunnel() {
            return parent;
        }

        @Override
        public void periodic() {

            super.periodic();

        }

    }

    public class Pusher extends GBSubsystem {
        private WPI_TalonSRX pusher_right, pusher_left;

        private Funnel parent;

        private Pusher(Funnel parent) {
            this.parent = parent;
            pusher_right = new WPI_TalonSRX(RobotMap.Limbo2.Funnel.Motors.PUSHER_RIGHT_PORT);
            pusher_right.setInverted(RobotMap.Limbo2.Funnel.Motors.PUSHER_RIGHT_REVERSE);

            pusher_left = new WPI_TalonSRX(RobotMap.Limbo2.Funnel.Motors.PUSHER_LEFT_PORT);
            pusher_left.setInverted(RobotMap.Limbo2.Funnel.Motors.PUSHER_LEFT_REVERSE);
        }

        public Funnel getFunnel() {
            return parent;
        }

        @Override
        public void periodic() {

            super.periodic();

        }

        public WPI_TalonSRX getTalon() {
            // used solely for pigeonIMU, therefore there is not need for independent getters for right and left
            return pusher_right;
        }
    }

}
