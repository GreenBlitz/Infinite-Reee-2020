package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
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
import edu.greenblitz.bigRodika.commands.shifter.ToPower;
import edu.greenblitz.bigRodika.commands.shifter.ToSpeed;
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

//       initTestButtons();
       initOfficalButtons();
    }


    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons() {

        mainJoystick.A.whenPressed(new CheckMaxRot(0.5));

    }

    private void initOfficalButtons() {

//        mainJoystick.L1.whenReleased(new ToggleShift());
        List<State> locations = new ArrayList<>(2);
        locations.add(new State(0, 0, 0, 0, 0));
        locations.add(new State(0.0, 2.0, 0, 0, 0));
        Follow2DProfileCommand command =  new Follow2DProfileCommand(locations, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 1, false);
        command.setSendData(true);
        mainJoystick.X.whenPressed(new ThreadedCommand(command, Chassis.getInstance()));

        mainJoystick.Y.whenPressed(new ShootAdjesant(mainJoystick.Y));

        mainJoystick.A.whenPressed(new CheckMaxLin(0.3));
        mainJoystick.B.whenPressed(new CheckMaxRot(0.3));

        mainJoystick.START.whenPressed(new ToSpeed());
        mainJoystick.BACK.whenPressed(new ToPower());



        // ---------------------------------------------------------------

//        secondStick.R1.whenPressed(new CompleteShoot(secondStick));
//        secondStick.R1.whenReleased(new ParallelCommandGroup(new StopShooter(),
//                                                             new ResetDome(-0.5)));

//        secondStick.L1.whileHeld(new InsertIntoShooter(1.0, 0.8, 0.6));
//        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
//                new StopInserter(), new StopRoller()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(
                        new RollByConstant(-0.5), new PushByConstant(-0.3), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()
        , new StopRoller()));

        secondStick.B.whenPressed(new ToggleExtender());
        secondStick.R1.whileHeld(new RollByConstant(1.0));
        secondStick.L1.whileHeld(new RollByConstant(-0.7));

        /*secondStick.START.whenPressed(new FullyAutoThreeStage(1650)); // 1650
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
*/
        secondStick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

        secondStick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));

//        secondStick.POV_LEFT.whileHeld(new MoveTurretByConstant(-0.3));
//
//        secondStick.POV_RIGHT.whileHeld(new MoveTurretByConstant(0.3));

        secondStick.X.whenPressed(new ResetDome(-0.3));

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