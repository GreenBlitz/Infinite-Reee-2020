package edu.greenblitz.bigRodika.commands.shifter;

import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.gblib.gears.Gear;

public class ToSpeed extends ShifterCommand {
    public ToSpeed(Shifter sh) {
        super(sh);
    }

    @Override
    public void initialize() {
        shifter.setShift(Gear.SPEED);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
