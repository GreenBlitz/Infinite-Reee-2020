package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.autonomous.ThreeStageForAutonomous;
import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftlyRadians;
import edu.greenblitz.bigRodika.commands.turret.TurretToFront;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Dome;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;

import java.util.ArrayList;
import java.util.List;

public class ThreeBallNoVision extends SequentialCommandGroup {

    public ThreeBallNoVision() {

        List<State> stateList = new ArrayList<>();
        stateList.add(new State(0, 0));
        stateList.add(new State(0, -1));

        addCommands(
                new DomeMoveByConstant(0.3).withTimeout(0.2),
                new ResetDome(-0.3),
//                new ExtendRoller(),
                new ParallelCommandGroup(
                        new ThreadedCommand(new Follow2DProfileCommand(
                                stateList,
                                RobotMap.Limbo2.Chassis.MotionData.CONFIG,
                                0.3, true
                        ), Chassis.getInstance())).withTimeout(4),
//                new PreShoot(new DumbAlign(4.0, .1, .3)),
//                        new TurretToFront(),
//                        new DomeApproachSwiftly(RobotMap.Limbo2.Dome.DOME.get(4.0)).withTimeout(1.5)
//                )
                new ParallelCommandGroup(
                        new TurretApproachSwiftlyRadians(-Math.toRadians(3.5),
                                new AbsoluteTolerance(Math.toRadians(1.0))),
                        new DomeApproachSwiftly(0.5)).withTimeout(2),
                new ParallelCommandGroup(
                        new InsertIntoShooter(1, 0.3, 0.1),
                        new ThreeStageForAutonomous(3550, 0.6)
                )
        );

        m_requirements.remove(Dome.getInstance());

    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
//        SmartDashboard.putBoolean("Auto interrupted", interrupted);
    }
}
