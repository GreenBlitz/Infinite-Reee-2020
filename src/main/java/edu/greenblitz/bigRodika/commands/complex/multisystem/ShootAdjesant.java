package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class ShootAdjesant extends SequentialCommandGroup {

    public ShootAdjesant(JoystickButton bind) {

        List<State> goBack = new ArrayList<>();
        goBack.add(new State(0, 0));
        goBack.add(new State(0,0.4));

        addCommands(
                new ParallelRaceGroup(
                        new DomeApproachSwiftly(0.1), // was 0.1
                        new ThreadedCommand(
                                new Follow2DProfileCommand(goBack,
                                        RobotMap.Limbo2.Chassis.MotionData.CONFIG,
                                        0.3, false),
                                Chassis.getInstance())
                ),
                new ExtendRoller(),
                new GBCommand() {
                    @Override
                    public boolean isFinished() {
                        return !bind.get();
                    }
                }
        );
    }
}
