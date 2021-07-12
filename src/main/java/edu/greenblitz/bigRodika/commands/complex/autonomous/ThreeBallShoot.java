package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.turret.resets.ResetEncoderWhenInSide;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class ThreeBallShoot extends SequentialCommandGroup{
    public ThreeBallShoot(){
        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, 1.0));
        addCommands(
                new ResetEncoderWhenInSide(),
                (Command) new Follow2DProfileCommand(hardCodedShit, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false)
        );
    }
}
