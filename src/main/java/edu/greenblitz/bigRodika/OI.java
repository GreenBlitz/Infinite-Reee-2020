package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.motion.DumbAlign;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShootAndWait;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
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
import edu.greenblitz.bigRodika.commands.shifter.ToPower;
import edu.greenblitz.bigRodika.commands.shifter.ToSpeed;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootByDashboard;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.commands.turret.StopTurret;
import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftlyRadians;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;

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

//        initTestButtons();
        initOfficalButtons();
    }


    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons() {

        //mainJoystick.R1.whileHeld(new ChainFetch(5, mainJoystick));
        //mainJoystick.R1.whenReleased(new ArcadeDrive(mainJoystick));
        mainJoystick.B.whenPressed(new DumbAlign(4.0, 0.1, 0.3));

        mainJoystick.POV_UP.whenPressed(new CheckMaxLin(0.3));
        mainJoystick.POV_DOWN.whenPressed(new CheckMaxRot(0.3));

        List<State> states = new ArrayList<>();
        states.add(new State(0, 0));
        states.add(new State(0, -2, 0));

        Follow2DProfileCommand prof = new Follow2DProfileCommand(states, RobotMap.Limbo2.Chassis.MotionData.CONFIG
                , 0.3, true);

//        mainJoystick.A.whenPressed(new ThreadedCommand(prof, Chassis.getInstance()));

//        secondStick.L3.whenPressed(new ResetEncoderWhenInFront());
        secondStick.A.whenPressed(new TurretByVision(VisionMaster.Algorithm.FEEDING_STATION));
        secondStick.A.whenReleased(new StopTurret());

        secondStick.POV_UP.whenPressed(new DomeMoveByConstant(0.3));
        secondStick.POV_UP.whenReleased(new DomeMoveByConstant(0));
        secondStick.POV_DOWN.whenPressed(new DomeMoveByConstant(-0.3));
        secondStick.POV_DOWN.whenReleased(new DomeMoveByConstant(0));


        /*secondStick.START.whenPressed(new MoveTurretByConstant(0.3));
        secondStick.START.whenReleased(new StopTurret());

        secondStick.BACK.whenPressed(new MoveTurretByConstant(-0.3));
        secondStick.BACK.whenReleased(new StopTurret());*/

        mainJoystick.A.whenPressed(new TurretApproachSwiftlyRadians(0));
        mainJoystick.A.whenReleased(new StopTurret());
    }

    private void initOfficalButtons() {

//        mainJoystick.R1.whileHeld(new ChainFetch(5, mainJoystick));
//        mainJoystick.R1.whenReleased(new ArcadeDrive(mainJoystick));

        List<Double> rads = new ArrayList<>();

        rads.add(0.5);
        rads.add(4.0);
        rads.add(4.5);
        rads.add(6.3);

        mainJoystick.A.whileHeld(
                new PreShootAndWait(
                        new PreShoot(
                                new DumbAlign(rads, .1, .3))));
        mainJoystick.A.whenReleased(new ParallelCommandGroup(new ResetDome(), new StopTurret()));
//        mainJoystick.START.whenPressed(new ToggleShift());

        mainJoystick.B.whileHeld(new TurretByVision(VisionMaster.Algorithm.HEXAGON));
//        mainJoystick.B.whenReleased(new StopTurret());

        mainJoystick.START.whenPressed(new ToSpeed());
        mainJoystick.BACK.whenPressed(new ToPower());

        mainJoystick.POV_UP.whenPressed(new DomeApproachSwiftly(RobotMap.Limbo2.Dome.DOME.get(6.3)));
        mainJoystick.POV_DOWN.whenPressed(new TurretApproachSwiftlyRadians(0));

        // ---------------------------------------------------------------

        secondStick.R1.whenPressed(new FullyAutoThreeStage(2500, 0.45));
        secondStick.R1.whenReleased(new ParallelCommandGroup(new StopShooter(),
                new ResetDome()));

//        secondStick.R1.whenPressed(new FullyAutoThreeStage(2600, 0.5));
//        secondStick.R1.whenReleased(new ParallelCommandGroup(new StopShooter(),
//                new ResetDome()));

        secondStick.L1.whileHeld(new InsertIntoShooter(1, 0.5, 0.6));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(
                new RollByConstant(-0.5), new PushByConstant(-0.3), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()
                , new StopRoller()));

        secondStick.B.whenPressed(new ToggleExtender());
        secondStick.A.whileHeld(new RollByConstant(1.0));
        secondStick.R3.whileHeld(new RollByConstant(-1.0));

        secondStick.START.whenPressed(new ParallelCommandGroup(
                new TurretApproachSwiftlyRadians(-Math.PI),
                new DomeApproachSwiftly(0.15)
        ));

        secondStick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

        secondStick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));

        secondStick.POV_LEFT.whileHeld(new MoveTurretByConstant(-0.2));

        secondStick.POV_RIGHT.whileHeld(new MoveTurretByConstant(0.2));

        secondStick.BACK.whenPressed(new ResetDome(-0.3));

        secondStick.L3.whenPressed(new GBCommand() {
            @Override
            public void initialize() {
                CommandScheduler.getInstance().cancelAll();
            }

            @Override
            public boolean isFinished() {
                return true;
            }
        });

        secondStick.X.whileHeld(new ParallelCommandGroup(
                new RollByConstant(0.6), new PushByConstant(0.5), new InsertByConstant(1)));

        mainJoystick.X.whenPressed(new ShootBySimplePid(
                new PIDObject(RobotMap.Limbo2.Shooter.SHOOTER_P,RobotMap.Limbo2.Shooter.SHOOTER_I,RobotMap.Limbo2.Shooter.SHOOTER_D, RobotMap.Limbo2.Shooter.SHOOTER_F ),
                2000));

        mainJoystick.Y.whenPressed(new ParallelCommandGroup(new ResetDome(-0.3), new StopShooter()));
    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}