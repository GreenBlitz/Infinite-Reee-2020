package edu.greenblitz.bigRodika.commands.shifter;

import edu.greenblitz.gblib.gears.Gear;

public class ToPower extends ShifterCommand {

    @Override
    public void initialize() {
        shifter.setShift(Gear.POWER);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
