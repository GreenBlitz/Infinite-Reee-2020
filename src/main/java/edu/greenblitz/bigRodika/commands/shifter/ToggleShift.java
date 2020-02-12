package edu.greenblitz.bigRodika.commands.shifter;


import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.gblib.gears.Gear;

/**
 * This command switches the Gear from the state it is currently in.
 * This command uses the Shifter subsystem.
 * The command will stopRolling as soon as the shift is switched.
 */

public class ToggleShift extends ShifterCommand {

    @Override
    public void execute() {
        shifter.setShift(shifter.getCurrentGear() == Gear.POWER ? Gear.SPEED : Gear.POWER);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
