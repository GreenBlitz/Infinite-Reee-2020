package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

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
        extender.extender.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        extender.extender.set(DoubleSolenoid.Value.kReverse);
    }

    public boolean isExtended() {
        return extender.extender.get().equals(DoubleSolenoid.Value.kForward);
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

    private class IntakeSubsystem implements Subsystem {
        public Intake getIntake() {
            return Intake.this;
        }
    }

    public class Roller extends IntakeSubsystem {

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

        private DoubleSolenoid extender;

        private Extender() {
            extender = new DoubleSolenoid(RobotMap.Limbo2.Intake.PCM, RobotMap.Limbo2.Intake.Solenoid.FORWARD, RobotMap.Limbo2.Intake.Solenoid.REVERSE);
        }

        @Override
        public void periodic() {
        }
    }
}
