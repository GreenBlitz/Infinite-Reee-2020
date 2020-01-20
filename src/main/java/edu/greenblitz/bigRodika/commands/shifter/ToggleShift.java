package edu.greenblitz.bigRodika.commands.shifter;


import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.gblib.command.GBCommand;

/**
 * This command switches the Gear from the state it is currently in.
 * This command uses the Shifter subsystem.
 * The command will stopRolling as soon as the shift is switched.
 */

public class ToggleShift extends GBCommand {

    private Shifter sh;

    public ToggleShift(Shifter s){
        super(s);
        sh = s;
    }

    @Override
    public void execute() {
        sh.setShift(sh.getCurrentGear() == Shifter.Gear.POWER ? Shifter.Gear.SPEED : Shifter.Gear.POWER);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
