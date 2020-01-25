package edu.greenblitz.bigRodika.commands.shifter;


import edu.greenblitz.bigRodika.subsystems.Shifter;

/**
 * This command switches the Gear from the state it is currently in.
 * This command uses the Shifter subsystem.
 * The command will stopRolling as soon as the shift is switched.
 */

public class ToggleShift extends ShifterCommand {

    public ToggleShift(Shifter shifter){
        super(shifter);
        this.shifter = shifter;
    }

    @Override
    public void execute() {
        shifter.setShift(shifter.getCurrentGear() == Shifter.Gear.POWER ? Shifter.Gear.SPEED : Shifter.Gear.POWER);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
