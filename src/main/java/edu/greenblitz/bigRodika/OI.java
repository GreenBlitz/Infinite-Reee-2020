package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.complex.multisystem.CompleteShoot;
import edu.greenblitz.bigRodika.commands.complex.multisystem.ShootAdjesant;
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
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.*;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    public static final boolean DEBUG = true;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN,
                RobotMap.Limbo2.Joystick.MAIN_DEADZONE);
        secondStick = new SmartJoystick(RobotMap.Limbo2.Joystick.SIDE,
                RobotMap.Limbo2.Joystick.SIDE_DEADZONE);

        if (DEBUG) {
            initTestButtons();
        } else {
            initOfficalButtons();
        }
    }


    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons() {
        mainJoystick.A.whenPressed(new CheckMaxLin(0.8));
        mainJoystick.B.whenPressed(new CheckMaxRot(0.8));

        mainJoystick.START.whenPressed(new ToSpeed());
        mainJoystick.BACK.whenPressed(new ToPower());

        List<State> locations = new ArrayList<>(2);
        locations.add(new State(0, 0, 0, 0, 0));
        locations.add(new State(2.0, 2.0, -0.25*Math.PI, 0, 0));
        Follow2DProfileCommand command = new Follow2DProfileCommand(locations, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3
                , false);
        command.setSendData(true);
        mainJoystick.POV_UP.whenPressed(new ThreadedCommand(command, Chassis.getInstance()));

    }

    private void initOfficalButtons() {

        mainJoystick.L1.whenReleased(new ToggleShift());

        mainJoystick.X.whenPressed(new ResetDome(-0.3));

        mainJoystick.Y.whenPressed(new ShootAdjesant(mainJoystick.Y));

        mainJoystick.START.whenPressed(new ToSpeed());
        mainJoystick.BACK.whenPressed(new ToPower());

        // ---------------------------------------------------------------

        secondStick.R1.whenPressed(new ShootByConstant(0.2));
        secondStick.R1.whenReleased(new ParallelCommandGroup(new StopShooter(),
                new ResetDome(-0.5)));

        secondStick.L1.whileHeld(new InsertIntoShooter(1.0, 0.8, 0.6));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(
                new RollByConstant(0.5), new PushByConstant(0.8), new InsertByConstant(0.6)));
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

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}