package edu.greenblitz.bigRodika.commands.shifter;

import edu.greenblitz.bigRodika.subsystems.Shifter;

public class ToPower extends ShifterCommand {
    public ToPower(Shifter sh) {
        super(sh);
    }

    @Override
    public void initialize() {
        shifter.setShift(Shifter.Gear.POWER);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
