package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Intake {

    private static Intake instance;
    private Roller roller;
    private Extender extender;

    private Intake() {
        roller = new Intake.Roller();
        extender = new Intake.Extender();
    }

    public static void init() {
        if (instance == null) {
            instance = new Intake();
            CommandScheduler.getInstance().registerSubsystem(instance.roller);
            CommandScheduler.getInstance().registerSubsystem(instance.extender);
        }
    }

    public static Intake getInstance() {
        return instance;
    }

    public void moveRoller(double power) {
        roller.roller.set(power);
    }

    public void extend() {
        extender.extenderLeft.set(DoubleSolenoid.Value.kForward);
        extender.extenderRight.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        extender.extenderLeft.set(DoubleSolenoid.Value.kReverse);
        extender.extenderRight.set(DoubleSolenoid.Value.kReverse);
    }

    public boolean isExtended() {
        return extender.extenderLeft.get().equals(DoubleSolenoid.Value.kForward);
    }

    public void toggleExtender() {
        if (isExtended()) {
            retract();
        } else {
            extend();
        }
    }

    public Roller getRoller() {
        return roller;
    }

    public Extender getExtender() {
        return extender;
    }

    private class IntakeSubsystem extends GBSubsystem {
        public Intake getIntake() {
            return Intake.this;
        }
    }

    private class Roller extends IntakeSubsystem {

        private WPI_TalonSRX roller;

        private Roller() {
            roller = new WPI_TalonSRX(RobotMap.Limbo2.Intake.Motors.ROLLER_PORT);
            roller.setInverted(RobotMap.Limbo2.Intake.Motors.IS_REVERSED);
            roller.setNeutralMode(NeutralMode.Coast);
        }

        @Override
        public void periodic() {
        }
    }

    public class Extender extends IntakeSubsystem {

        private DoubleSolenoid extenderLeft;
        private DoubleSolenoid extenderRight;

        private Extender() {
            extenderLeft = new DoubleSolenoid(RobotMap.Limbo2.Intake.PCM,
                    RobotMap.Limbo2.Intake.Solenoid.FORWARD_LEFT,
                    RobotMap.Limbo2.Intake.Solenoid.REVERSE_LEFT);
            extenderRight = new DoubleSolenoid(RobotMap.Limbo2.Intake.PCM,
                    RobotMap.Limbo2.Intake.Solenoid.FORWARD_RIGHT,
                    RobotMap.Limbo2.Intake.Solenoid.REVERSE_RIGHT);
        }

        private void setValue(DoubleSolenoid.Value value) {
            extenderRight.set(value);
            extenderLeft.set(value);
        }

        public void extend() {
            setValue(DoubleSolenoid.Value.kForward);
        }

        public void retract() {
            setValue(DoubleSolenoid.Value.kReverse);
        }


        @Override
        public void periodic() {
            super.periodic();
            putString("Left", extenderLeft.get().toString());
            putString("Right", extenderRight.get().toString());
        }
    }
}
