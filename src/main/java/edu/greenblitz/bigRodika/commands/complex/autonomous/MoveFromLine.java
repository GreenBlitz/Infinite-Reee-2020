package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class MoveFromLine extends ChassisCommand {

    private long tstart;

    public MoveFromLine(){
        tstart = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        Chassis.getInstance().moveMotors(-0.3,-0.3);
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - tstart > 2000;
    }
}
