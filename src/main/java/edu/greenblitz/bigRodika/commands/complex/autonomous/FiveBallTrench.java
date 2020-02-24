package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.autonomous.ThreeStageForAutonomous;
import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftlyRadians;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class FiveBallTrench extends SequentialCommandGroup {

    public FiveBallTrench() {
        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, -3.9));

        List<State> secondHardCodedShit = new ArrayList<>();
        secondHardCodedShit.add(new State(0, 0));
        secondHardCodedShit.add(new State(0, 0.5));

        addCommands(
                new DomeMoveByConstant(0.3).withTimeout(0.2),
                new ResetDome(-0.3),
                new ExtendRoller(),
                new WaitCommand(0.4),
                new ParallelRaceGroup(
                        new ThreadedCommand(new Follow2DProfileCommand(hardCodedShit,
                                RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, true),
                                Chassis.getInstance()),
                        new SequentialCommandGroup(new WaitCommand(0.6), new RollByConstant(1.0))
                ),
                new StopRoller(),
                new WaitCommand(0.4),
//                new PreShoot(new DumbAlign(6.3, .1, .3)),
                new ThreadedCommand(new Follow2DProfileCommand(secondHardCodedShit,
                        RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false),
                        Chassis.getInstance()),
                new TurretApproachSwiftlyRadians(Math.toRadians(-10)).withInterrupt(() ->
                        VisionMaster.getInstance().isLastDataValid() &&
                                Math.abs(VisionMaster.getInstance().getVisionLocation().getRelativeAngle()) < 10).withTimeout(2),
                new TurretByVision(VisionMaster.Algorithm.HEXAGON).withInterrupt(() ->
                        VisionMaster.getInstance().isLastDataValid() &&
                                Math.abs(VisionMaster.getInstance().getVisionLocation().getRelativeAngle()) < 1).withTimeout(3),
                new DomeApproachSwiftly(5.0).withTimeout(1),
                new ParallelCommandGroup(
                        new InsertIntoShooter(1, 0.5, 0.6),
                        new ThreeStageForAutonomous(3700, 0.65))
        );
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        SmartDashboard.putBoolean("Auto interrupted", interrupted);
    }
}