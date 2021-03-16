package edu.greenblitz.bigRodika.commands.shifter;

import edu.greenblitz.gblib.gears.Gear;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.apache.logging.log4j.core.net.SslSocketManager;

public class ToSpeed extends ShifterCommand {

    @Override
    public void initialize() {
        SmartDashboard.putBoolean("is start", true);
        shifter.setShift(Gear.SPEED);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
