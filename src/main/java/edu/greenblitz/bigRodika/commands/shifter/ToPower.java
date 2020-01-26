package edu.greenblitz.bigRodika.commands.shifter;

import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.gblib.gears.Gear;

public class ToPower extends ShifterCommand {
    public ToPower(Shifter sh) {
        super(sh);
    }

    @Override
    public void initialize() {
        shifter.setShift(Gear.POWER);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
