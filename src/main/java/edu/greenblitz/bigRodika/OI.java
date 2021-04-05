package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.Approach;
import edu.greenblitz.bigRodika.commands.chassis.approaches.ApproachAccurate;
import edu.greenblitz.bigRodika.commands.complex.multisystem.CompleteShootSkills;
import edu.greenblitz.bigRodika.commands.complex.multisystem.ShootAdjesant;
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
import edu.greenblitz.bigRodika.commands.shifter.ToPower;
import edu.greenblitz.bigRodika.commands.shifter.ToSpeed;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretToFront;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.greenblitz.motion.base.TwoTuple;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;

import java.util.function.Supplier;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    public JoystickButton pusherAlternate;
    public JoystickButton completeShootStop;

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

        pusherAlternate = mainJoystick.B;
        completeShootStop = mainJoystick.L1;

        mainJoystick.R1.whenPressed(new CompleteShootSkills());

        Supplier<Double> power = () -> Math.abs(RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_NEGATIVE_180 - Turret.getInstance().getRawTicks()) >
                Math.abs(RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD - Turret.getInstance().getRawTicks()) ? 0.05:-0.05;

        mainJoystick.A.whenPressed(new ApproachAccurate(() -> {
            double visionDist = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
            TwoTuple<TwoTuple<Double, double[]>, TwoTuple<Double, double[]>> dist = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(visionDist);
            double d = Math.abs(dist.getFirst().getFirst() - visionDist) < Math.abs(dist.getSecond().getFirst() - visionDist) ? dist.getFirst().getFirst() : dist.getSecond().getFirst();
            SmartDashboard.putNumber("Desired distance", d);
            return d;
        }, power, 0.03));


        /*mainJoystick.B.whenPressed(new ApproachAccurate(() -> {
            double visionDist = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
            TwoTuple<TwoTuple<Double, double[]>, TwoTuple<Double, double[]>> dist = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(visionDist);
            double d = Math.abs(dist.getFirst().getFirst() - visionDist) < Math.abs(dist.getSecond().getFirst() - visionDist) ? dist.getFirst().getFirst() : dist.getSecond().getFirst();
            SmartDashboard.putNumber("Desired distance", d);
            return d;
        }, 0.05, 0.03));*/

        mainJoystick.X.whenPressed(new TurretToFront());

        //-----------------------------------------------------

//        secondStick.B.whenPressed(new ToggleExtender());
        secondStick.R1.whenPressed(new RollByConstant(0.5));

        mainJoystick.POV_UP.whileHeld(new DomeMoveByConstant(0.5));
        mainJoystick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.2));

        mainJoystick.POV_LEFT.whileHeld(new MoveTurretByConstant(0.3));
        mainJoystick.POV_RIGHT.whileHeld(new MoveTurretByConstant(-0.3));
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