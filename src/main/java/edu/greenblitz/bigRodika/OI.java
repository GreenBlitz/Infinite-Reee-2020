package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.complex.multisystem.ShootAdjesant;
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
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondJoystick;

    public static final boolean DEBUG = true;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN,
                RobotMap.Limbo2.Joystick.MAIN_DEADZONE);
        secondJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.SIDE,
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
        mainJoystick.X.whenPressed(new FullyAutoThreeStage(2500));
        secondJoystick.A.whenPressed(new ParallelCommandGroup(
                new DomeApproachSwiftly(0.345),
                new FullyAutoThreeStage(2000)));
        secondJoystick.B.whenPressed(new ParallelCommandGroup(
                new DomeApproachSwiftly(0.36),
                new FullyAutoThreeStage(2100)));
        secondJoystick.Y.whenPressed(new ParallelCommandGroup(
                new DomeApproachSwiftly(0.403),
                new FullyAutoThreeStage(2350)));
        secondJoystick.X.whenPressed(new ParallelCommandGroup(
                new DomeApproachSwiftly(0.486),
                new FullyAutoThreeStage(2800)));

        mainJoystick.Y.whenPressed(new StopShooter());

        mainJoystick.A.whileHeld(new
                ParallelCommandGroup(new RollByConstant(0.5), new PushByConstant(0.5), new InsertByConstant(0.6)));
        mainJoystick.A.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));


        mainJoystick.B.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.5), new InsertByConstant(-0.6)));
        mainJoystick.B.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        mainJoystick.POV_UP.whileHeld(new DomeMoveByConstant(0.4));

        mainJoystick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.4));

        mainJoystick.POV_LEFT.whileHeld(new MoveTurretByConstant(-0.2));

        mainJoystick.POV_RIGHT.whileHeld(new MoveTurretByConstant(0.2));

        mainJoystick.L1.whenPressed(new ResetDome(-0.3));
    }

    private void initOfficalButtons() {

        mainJoystick.L1.whenReleased(new ToggleShift());

        mainJoystick.X.whenPressed(new ResetDome(-0.3));

        mainJoystick.Y.whenPressed(new ShootAdjesant(mainJoystick.Y));

        mainJoystick.START.whenPressed(new ToSpeed());
        mainJoystick.BACK.whenPressed(new ToPower());

        // ---------------------------------------------------------------

        secondJoystick.R1.whenPressed(new ShootByConstant(0.2, "fuck you bitch"));
        secondJoystick.R1.whenReleased(new ParallelCommandGroup(new StopShooter(),
                new ResetDome(-0.5)));

        secondJoystick.L1.whileHeld(new InsertIntoShooter(1.0, 0.8, 0.6));
        secondJoystick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondJoystick.Y.whileHeld(new
                ParallelCommandGroup(
                new RollByConstant(0.5), new PushByConstant(0.8), new InsertByConstant(0.6)));
        secondJoystick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()
                , new StopRoller()));

        secondJoystick.B.whenPressed(new ToggleExtender());
        secondJoystick.A.whileHeld(new RollByConstant(1.0));
        secondJoystick.R3.whileHeld(new RollByConstant(-0.7));

        secondJoystick.START.whenPressed(new FullyAutoThreeStage(1650)); // 1650
        secondJoystick.START.whenReleased(new StopShooter());

        secondJoystick.X.whenPressed(new ShootByConstant(
                Shooter.getInstance().getDesiredPower(2000), "fuck you bitch 2"
        ));
        secondJoystick.X.whenReleased(new GBCommand() {
            @Override
            public void initialize() {
                if (!secondJoystick.R1.get()) new StopShooter().schedule();
            }

            @Override
            public boolean isFinished() {
                return true;
            }
        });

        secondJoystick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

        secondJoystick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));

        secondJoystick.POV_LEFT.whileHeld(new MoveTurretByConstant(-0.2));

        secondJoystick.POV_RIGHT.whileHeld(new MoveTurretByConstant(0.2));

        secondJoystick.BACK.whenPressed(new ResetDome(-0.3));


    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondJoystick;
    }
}