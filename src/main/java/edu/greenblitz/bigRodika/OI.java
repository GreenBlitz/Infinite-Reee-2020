package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.BrakeChassis;
import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.motion.ChainFetch;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.chassis.turns.TurnToVision;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftlyTesting;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootByDashboard;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.ThreeStageShoot;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.test.ThreeStageTesting;
import edu.greenblitz.bigRodika.commands.turret.*;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;

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

        secondStick.START.whenPressed(new ResetDome(-0.22));

//        secondStick.L1.whenPressed(new ShootByConstant(0.6));
//        secondStick.L1.whenReleased(new StopShooter());

//        secondStick.R1.whenPressed(
//                new ParallelCommandGroup(
//                        new InsertByConstant(0.5),
//                        new PushByConstant(0.5)
//                )
//                );
//        secondStick.R1.whenReleased(
//                new ParallelCommandGroup(new StopInserter(), new StopPusher())
//        );


//        secondStick.B.whenPressed(new DomeApproachSwiftly(0.5,
//                new AbsoluteTolerance(0.01)));
//
//        mainJoystick.B.whenPressed(new DomeMoveByConstant(0.2));
//        mainJoystick.B.whenReleased(new DomeMoveByConstant(0));

        secondStick.R1.whenPressed(new TurretApproachSwiftlyByRadiansRelative(Math.PI/8));
        secondStick.R1.whenReleased(new StopTurret());
//
//        mainJoystick.B.whenPressed(new TurretApproachSwiftly(0.1));
//        mainJoystick.B.whenReleased(new StopTurret());

//        secondStick.X.whenPressed(new DomeMoveByConstant(-0.2));
//        secondStick.X.whenReleased(new DomeMoveByConstant(0));

        //mainJoystick.A.whenPressed(new CheckMaxRot(0.8));

        secondStick.L1.whenPressed(new UnsafeResetTurret(-0.2));
        secondStick.L1.whenReleased(new StopTurret());

        secondStick.X.whenPressed(new TurretByVision(
                VisionMaster.Algorithm.FEEDING_STATION));
        secondStick.X.whenReleased(new StopTurret());
//        mainJoystick.X.whenPressed(new TurretToFront());
//        mainJoystick.X.whenReleased(new StopTurret());

        secondStick.A.whenPressed(new MoveTurretByConstant(0.2));
        secondStick.A.whenReleased(new MoveTurretByConstant(0));



        secondStick.Y.whenPressed(new MoveTurretByConstant(-0.2));
        secondStick.Y.whenReleased(new MoveTurretByConstant(0));

//        mainJoystick.Y.whenPressed(new CheckMaxLin(0.8));
//        mainJoystick.Y.whenReleased(new BrakeChassis());

        List<State> path = new ArrayList<>();

        path.add(new State(0,0));
        path.add(new State(0,2));
        Follow2DProfileCommand prof = new Follow2DProfileCommand(
                path, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.5 , false);

        //mainJoystick.Y.whenPressed(new ThreadedCommand(prof, Chassis.getInstance()));

        List<State> path2 = new ArrayList<>();

        path2.add(new State(0,0));
        path2.add(new State(1,1, Math.PI/2));
        Follow2DProfileCommand prof2 = new Follow2DProfileCommand(
                path2, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.5 , false);

        //mainJoystick.A.whenPressed(new ThreadedCommand(prof2, Chassis.getInstance()));
    }

    private void initOfficalButtons() {

        mainJoystick.R1.whileHeld(new ChainFetch(5, mainJoystick));
        mainJoystick.R1.whenReleased(new ArcadeDrive(mainJoystick));

        mainJoystick.L1.whileHeld(new PreShoot(4.0, false));

        secondStick.R1.whenPressed(new FullyAutoThreeStage(4500, 0.8));
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new InsertIntoShooter(0.5, 0.7, 0.5));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.7), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        secondStick.B.whenPressed(new ToggleExtender());
        secondStick.A.whileHeld(new RollByConstant(0.5));

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}