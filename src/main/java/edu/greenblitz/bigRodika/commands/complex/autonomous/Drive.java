package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.DumbAlign;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.autonomous.ThreeStageForAutonomous;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class Drive extends
    SequentialCommandGroup{

    public Drive() {

        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, -1));

        addCommands(
                new ThreadedCommand(new Follow2DProfileCommand(hardCodedShit,
                                RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, true),
                                Chassis.getInstance()),
                new DumbAlign(4.0, .1, .3),
                new ParallelCommandGroup(
                        new InsertIntoShooter(1, 0.5, 0.1),
                        new ThreeStageForAutonomous(3700, 0.65)
                ));


    }

        @Override
        public void end(boolean interrupted) {
        super.end(interrupted);
        SmartDashboard.putBoolean("Auto interrupted2", interrupted);
    }
}
