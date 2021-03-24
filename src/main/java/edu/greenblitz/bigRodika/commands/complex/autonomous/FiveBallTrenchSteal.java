package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class FiveBallTrenchSteal extends SequentialCommandGroup {

    public FiveBallTrenchSteal() {
        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, 1.0) );

        List<State> secondHardCodedShit = new ArrayList<>();
        secondHardCodedShit.add(new State(0, 0));
        secondHardCodedShit.add(new State(0, 1.5));

        addCommands(
                new DomeMoveByConstant(0.4).withTimeout(0.2),
                new ParallelCommandGroup(
                        new ResetDome(-0.3),
                        new ThreadedCommand(new Follow2DProfileCommand(hardCodedShit,
                                RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false),
                                Chassis.getInstance())
                ));
//                new StopRoller(),
//                new WaitCommand(0.2),
//                new ParallelCommandGroup(
//                        new ThreadedCommand(new Follow2DProfileCommand(secondHardCodedShit,
//                                RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false),
//                                Chassis.getInstance())
//                ));
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
//        SmartDashboard.putBoolean("Auto interrupted", interrupted);
    }
}