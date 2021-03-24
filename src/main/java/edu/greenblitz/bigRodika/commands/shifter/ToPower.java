package edu.greenblitz.bigRodika.commands.shifter;

import edu.greenblitz.gblib.gears.Gear;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToPower extends ShifterCommand {

    @Override
    public void initialize() {
        shifter.setShift(Gear.POWER);
    }

    @Override
    public void execute() {
//        SmartDashboard.putBoolean("is start", false);
        shifter.setShift(Gear.POWER);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
