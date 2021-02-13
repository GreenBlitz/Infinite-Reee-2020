package edu.greenblitz.bigRodika.subsystems;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.shifter.ToPower;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.gears.Gear;
import edu.greenblitz.gblib.gears.GearDependentValue;
import edu.greenblitz.gblib.gears.GlobalGearContainer;
import edu.greenblitz.gblib.sendables.SendableDoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * This class is in charge of the shifter subsystem of the robot.
 * This subsystem includes a DoubleSolenoid.
 * It is important to note that this subsystem is very reliant on the Chassis subsystem, as it changes the gear ratio of that subsystem.
 *
 * @see Chassis
 * @see DoubleSolenoid
 */

public class Shifter extends GBSubsystem {

    private static Shifter instance;

    private SendableDoubleSolenoid m_piston;
    private Gear m_currentShift = Gear.POWER;
    private GearDependentValue<DoubleSolenoid.Value> shiftValue =
            new GearDependentValue<>(DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kReverse);
//    POWER(DoubleSolenoid.Value.kForward),
//    SPEED(DoubleSolenoid.Value.kReverse);


    /**
     * This constructor constructs the piston.
     */
    private Shifter() {

        m_piston = new SendableDoubleSolenoid(RobotMap.Limbo2.Chassis.Shifter.PCM,
                RobotMap.Limbo2.Chassis.Shifter.Solenoid.FORWARD,
                RobotMap.Limbo2.Chassis.Shifter.Solenoid.REVERSE);


    }

    /**
     * This function creates a new instance of this class.
     */
    public static void init() {
        if (instance == null) {
            instance = new Shifter();
//            instance.setDefaultCommand(new ToPower());
        }

    }

    /**
     * This function returns an instance of the class as long as it isn't null.
     *
     * @return The current instance of the class
     */
    public static Shifter getInstance() {
        init();
        return instance;
    }

    /**
     * This function sets the state of the piston based on the value received.
     *
     * @param state A value based off of the Gear enum. This value is then set as the state the piston is in.
     */
    public void setShift(Gear state) {
        //if (state.equals(Gear.POWER)) return; // TODO fucking shifter is broken kill me
        VisionMaster.ShifterState shifterState = state.isPower() ? VisionMaster.ShifterState.POWER : VisionMaster.ShifterState.SPEED;
        shifterState.setAsCurrent();
        m_currentShift = state;
        Chassis.getInstance().changeGear();
        GlobalGearContainer.getInstance().setGear(state);
        m_piston.set(shiftValue.getValue());
    }

    @Override
    public void periodic() {
        super.periodic();
        putString("Shift", m_currentShift.name());
    }

    public void toggleShift() {
        setShift(getCurrentGear() == Gear.POWER ? Gear.SPEED : Gear.POWER);
    }

    /**
     * This function returns the current state of the piston through the Gear enum.
     *
     * @return The state of the piston through the Gear enum
     */
    public Gear getCurrentGear() {
        return GlobalGearContainer.getInstance().getGear();
    }
}
