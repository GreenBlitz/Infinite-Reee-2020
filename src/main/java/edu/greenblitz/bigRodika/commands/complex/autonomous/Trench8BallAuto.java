package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.commands.chassis.motion.GoFetch;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.autonomous.ThreeStageForAutonomous;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.greenblitz.motion.base.State;

public class Trench8BallAuto extends ParallelCommandGroup {


    public Trench8BallAuto() {

        addCommands(

                new ThreeStageForAutonomous(2950, 0.57),
                new SequentialCommandGroup(

                        new GoFetch(new State(0, -2.8, 0)),
                        new GoFetch(new State(0, -0.9, 0)),
                        new PreShoot(6.4),
                        new InsertIntoShooter(0.5, 0.7, 0.5),
                        new WaitCommand(5),
                        new StopRoller(),
                        new StopPusher(),

                        new GoFetch(new State(-Chassis.getInstance().getLocation().getX(), 4.6 - Chassis.getInstance().getLocation().getY(), 0)),
                        new GoFetch(new State(0, 1.6, 0)),
                        new PreShoot(6.4),
                        new InsertIntoShooter(0.5, 0.7, 0.5),
                        new WaitCommand(5),
                        new StopRoller(),
                        new StopPusher()
                )

        );


    }

}
