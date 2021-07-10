package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import static edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putString;

public class Intake {

    private static Intake instance;
    private Roller roller;
    private Extender extender;
    public static final double SAFE_ZONE = 0.46;

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
        extender.safeExtend();
    }

    public void retract() {
        extender.safeRetract();
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
            if(this.getCurrentCommand() == null) {
                double left = OI.getInstance().getSideStick().getAxisValue(SmartJoystick.Axis.LEFT_TRIGGER);
                double right = OI.getInstance().getSideStick().getAxisValue(SmartJoystick.Axis.RIGHT_TRIGGER);
                if(left > right) {
                    this.roller.set(-OI.getInstance().getSideStick().getAxisValue(SmartJoystick.Axis.LEFT_TRIGGER));
                } else {
                    this.roller.set(OI.getInstance().getSideStick().getAxisValue(SmartJoystick.Axis.RIGHT_TRIGGER));
                }
            }
        }

    }

    public class Extender extends IntakeSubsystem {

        private DoubleSolenoid extender;

        private Extender() {
            extender = new DoubleSolenoid(RobotMap.Limbo2.Intake.PCM,
                    RobotMap.Limbo2.Intake.Solenoid.FORWARD,
                    RobotMap.Limbo2.Intake.Solenoid.REVERSE);
        }

        private void setValue(DoubleSolenoid.Value value) {
            extender.set(value);
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
            putString("Extender", extender.get().toString());
        }

        public void safeRetract(){ //TODO: check safe angle zone
            double angle = Turret.getInstance().getTurretLocation();
            if(angle <= Turret.MIN_TICKS || angle >= SAFE_ZONE){
                retract();
            }
        }

        public void safeExtend() {
            double angle = Turret.getInstance().getTurretLocation();
            if(angle <= Turret.MIN_TICKS || angle >= SAFE_ZONE) {
                extend();
            }
        }
    }
}
