package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.commands.chassis.motion.DumbAlign;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.autonomous.ThreeStageForAutonomous;
import edu.greenblitz.bigRodika.subsystems.Dome;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBallSimple extends SequentialCommandGroup {

    public ThreeBallSimple() {

        addCommands(
                new DomeMoveByConstant(0.3).withTimeout(0.2),
                new ResetDome(-0.3),
                new ExtendRoller(),
                new WaitCommand(0.4),
                new PreShoot(new DumbAlign(4.0, .1, .3)),
                new ParallelCommandGroup(
                        new InsertIntoShooter(1, 0.3, 0.1),
                        new ThreeStageForAutonomous(3700, 0.65)
                )
        );

        m_requirements.remove(Dome.getInstance());

    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        SmartDashboard.putBoolean("Auto interrupted", interrupted);
    }
}
