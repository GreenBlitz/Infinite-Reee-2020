package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    public static final boolean DEBUG = false;

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
    }

    private void initOfficalButtons() {
/*
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
*/
        double p = RobotMap.Limbo2.Shooter.SHOOTER_P,
                i = RobotMap.Limbo2.Shooter.SHOOTER_I,
                d = RobotMap.Limbo2.Shooter.SHOOTER_D;
        mainJoystick.X.whenPressed(new FullyAutoThreeStage(1000));
        mainJoystick.Y.whenPressed(new StopShooter());

        mainJoystick.A.whileHeld(new
                ParallelCommandGroup(new PushByConstant(0.5), new InsertByConstant(0.6)));
        mainJoystick.A.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));


        mainJoystick.B.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.5), new InsertByConstant(-0.6)));
        mainJoystick.B.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));


        secondStick.A.whileHeld(new ShootByConstant(0.2));
        secondStick.A.whenReleased(new StopShooter());
        secondStick.B.whileHeld(new ShootByConstant(0.4));
        secondStick.B.whenReleased(new StopShooter());
        secondStick.Y.whileHeld(new ShootByConstant(0.6));
        secondStick.Y.whenReleased(new StopShooter());
        secondStick.X.whileHeld(new ShootByConstant(0.8));
        secondStick.X.whenReleased(new StopShooter());

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}