package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.turret.help.JustGoToTheFuckingTarget;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class SixBallTrench extends SequentialCommandGroup {

    public SixBallTrench() {
        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, -4.4));

        List<State> hardCodedShit3 = new ArrayList<>();
        hardCodedShit3.add(new State(0, 0));
        hardCodedShit3.add(new State(0, 1));


        addCommands(
                new DomeMoveByConstant(0.4).withTimeout(0.2),
                new ParallelCommandGroup(
                        new ResetDome(-0.3),
                        new ExtendRoller(),

                        new JustGoToTheFuckingTarget(() -> Math.toRadians(-12.5)).withTimeout(2).andThen(
                                new ParallelCommandGroup(
                                        new InsertIntoShooter(1, 0.5, 0.6),
                                        new CompleteShootAuto()).withTimeout(6.0)
                        )),
                new ParallelRaceGroup(
                        new ThreadedCommand(new Follow2DProfileCommand(hardCodedShit,
                                RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, true),
                                Chassis.getInstance()),
                        new SequentialCommandGroup(new WaitCommand(0.4), new RollByConstant(1.0))
                ),
                new ParallelCommandGroup(
                        new ThreadedCommand(new Follow2DProfileCommand(hardCodedShit3,
                                RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false),
                                Chassis.getInstance())),
                new WaitCommand(0.05),
                new ParallelCommandGroup(
                        new InsertIntoShooter(1, 0.5, 0.6),
                        new CompleteShootAuto())
        );
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        SmartDashboard.putBoolean("Auto interrupted", interrupted);
    }
}