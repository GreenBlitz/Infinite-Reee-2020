package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.climber.HoldElevator;
import edu.greenblitz.bigRodika.commands.climber.MoveHookByConstant;
import edu.greenblitz.bigRodika.commands.climber.ReleaseElevator;
import edu.greenblitz.bigRodika.commands.complex.multisystem.CompleteShoot;
import edu.greenblitz.bigRodika.commands.complex.multisystem.ShootAdjesant;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.*;
import edu.greenblitz.bigRodika.commands.turret.help.JustGoToTheFuckingTarget;
import edu.greenblitz.bigRodika.commands.turret.test.CheckMaxRotTurr;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.profiling.ProfilingConfiguration;

import java.util.ArrayList;

import java.util.List;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN,
                RobotMap.Limbo2.Joystick.MAIN_DEADZONE);
        secondStick = new SmartJoystick(RobotMap.Limbo2.Joystick.SIDE,
                RobotMap.Limbo2.Joystick.SIDE_DEADZONE);

        initTestButtons();
//        initOfficalButtons();
    }


    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons() {

        Follow2DProfileCommand.FollowerType follower = Follow2DProfileCommand.FollowerType.LIVE_FOLLOWER;
        double maxPower = 0.3;
        ProfilingConfiguration config = RobotMap.Limbo2.Chassis.MotionData.CONFIG;

        //testing driving forward
        List<State> l1 = new ArrayList<>();
        l1.add(new State(0, 0, 0, 0, 0));
        l1.add(new State(0, 2, 0, 0, 0));
        mainJoystick.X.whenPressed(new ThreadedCommand(new Follow2DProfileCommand(l1, config, maxPower,false, follower), Chassis.getInstance()));

        //testing 90 degrees rotation
        List<State> l2 = new ArrayList<>();
        l2.add(new State(0, 0, 0, 0, 0));
        l2.add(new State(2, 2, -Math.PI/2, 0, 0));
        Follow2DProfileCommand f2 = new Follow2DProfileCommand(l2, config, maxPower, false, follower);
        mainJoystick.Y.whenPressed(new ThreadedCommand(f2, Chassis.getInstance()));

        //driving like a 'tangent graph'
        List<State> l3  = new ArrayList<>();
        l3.add(new State(0, 0, 0, 0, 0));
        l3.add(new State(2, 2, 0, 0, 0));
        Follow2DProfileCommand f3 = new Follow2DProfileCommand(l3, config, maxPower, false, follower);
        mainJoystick.L1.whenPressed(new ThreadedCommand(f3, Chassis.getInstance()));

        //velocity check: the velocity changes at the middle point
        List<State> l4 = new ArrayList<>();
        l4.add(new State(0, 0, 0, 0, 0));
        l4.add(new State(0, 1, 0, 1, 0));
        l4.add(new State(0, 2, 0, 0, 0));
        Follow2DProfileCommand f4 = new Follow2DProfileCommand(l4, config, maxPower, false, follower);
        mainJoystick.R1.whenPressed(new ThreadedCommand(f4, Chassis.getInstance()));

        //reverse test
        List<State> l5 = new ArrayList<>();
        l5.add(new State(0, 0, 0, 0, 0));
        l5.add(new State(0, -2, 0, 0, 0));
        Follow2DProfileCommand f5 = new Follow2DProfileCommand(l5, config, maxPower, true, follower);
        mainJoystick.POV_UP.whenPressed(new ThreadedCommand(f5, Chassis.getInstance()));

        //driving in a circle clockwise
        // Ittai Sheffy recommends using the Localizer to check if the location had changed during the test because it
        // is not supposed to change.
        List<State> l6 = new ArrayList<>();
        l6.add(new State(0, 1, -Math.PI/2, 0, 0));
        l6.add(new State(1, 0, -Math.PI, 1, -Math.PI/4));
        l6.add(new State(0, -1, Math.PI/2, 1, -Math.PI/4));
        l6.add(new State(-1, 0, 0, 1, -Math.PI/4));
        l6.add(new State(0, 1, -Math.PI/2, 0, 0));
        Follow2DProfileCommand f6 = new Follow2DProfileCommand(l6, config, maxPower, false, follower);
        mainJoystick.POV_DOWN.whenPressed(new ThreadedCommand(f6, Chassis.getInstance()));

    }

    private void initOfficalButtons() {

//        mainJoystick.L1.whenReleased(new ToggleShift());

        mainJoystick.X.whenPressed(new ResetDome(-0.3));

        mainJoystick.Y.whenPressed(new ShootAdjesant(mainJoystick.Y));

//        mainJoystick.START.whenPressed(new ToSpeed());
//        mainJoystick.BACK.whenPressed(new ToPower());

        // ---------------------------------------------------------------

        secondStick.R1.whenPressed(new CompleteShoot(secondStick));
        secondStick.R1.whenReleased(new ParallelCommandGroup(new StopShooter(),
                                                             new ResetDome(-0.5)));

        secondStick.L1.whileHeld(new InsertIntoShooter(1.0, 0.8, 0.6));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(
                        new RollByConstant(-0.5), new PushByConstant(-0.3), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()
        , new StopRoller()));

        secondStick.B.whenPressed(new ToggleExtender());
        secondStick.A.whileHeld(new RollByConstant(1.0));
        secondStick.R3.whileHeld(new RollByConstant(-0.7));

        secondStick.START.whenPressed(new FullyAutoThreeStage(1650)); // 1650
        secondStick.START.whenReleased(new StopShooter());

        secondStick.X.whenPressed(new ShootByConstant(
                Shooter.getInstance().getDesiredPower(2000)
        ));
        secondStick.X.whenReleased(new GBCommand() {
            @Override
            public void initialize() {
                if (!secondStick.R1.get()) new StopShooter().schedule();
            }

            @Override
            public boolean isFinished() {
                return true;
            }
        });

        secondStick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

        secondStick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));

        secondStick.POV_LEFT.whileHeld(new MoveTurretByConstant(-0.2));

        secondStick.POV_RIGHT.whileHeld(new MoveTurretByConstant(0.2));

        secondStick.BACK.whenPressed(new ResetDome(-0.3));

        secondStick.L3.whenPressed(new GBCommand() {
            @Override
            public void initialize() {
                CommandScheduler.getInstance().cancelAll();
                Shooter.getInstance().shoot(0);
            }

            @Override
            public boolean isFinished() {
                return true;
            }
        });
    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}