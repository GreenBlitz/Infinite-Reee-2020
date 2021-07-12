package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class MoveFromLine extends GBCommand {

    public MoveFromLine(){
        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, 1.0) );
        new Follow2DProfileCommand(hardCodedShit, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false);
    }


}
