package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.driver.DriveUntilVision;
import edu.greenblitz.bigRodika.commands.chassis.motion.ChainFetch;
import edu.greenblitz.bigRodika.commands.chassis.motion.GoFetch;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.complex.autonomous.Trench8BallAuto;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.test.ThreeStageTesting;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;

import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.MotionProfile2D;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN, 0.08);
        secondStick = new SmartJoystick(1, 0.05);
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

        ProfilingData data = RobotMap.Limbo2.Chassis.MotionData.POWER.get("0.5");

        double vN = data.getMaxLinearVelocity();
        double aN = data.getMaxLinearAccel();
        double vNr = data.getMaxAngularVelocity();
        double aNr = data.getMaxAngularAccel();
        List<State> path = new ArrayList<>();
        path.add(new State(0,0,0));
        path.add(new State(0,-1,0));
//        Follow2DProfileCommand prof = new Follow2DProfileCommand(path,
//                .001, 400,
//                data,
//                1.0,
//                1.2*0.5, 1.0*0.5,
//                new PIDObject(0*0.6/vN,0*0.002/vN,0*12.0/aN, 1),0.01*vN,
//                new PIDObject(0*0.5/vNr,0,0*12.0/aNr, 1),0.01*vNr,
//                true);
//
//        mainJoystick.X.whenPressed(new ThreadedCommand(prof, Chassis.getInstance()));

        path.get(1).setY(1);
        Follow2DProfileCommand profi = new Follow2DProfileCommand(path,
                .001, 400,
                data,
                1.0,
                1.2*0.5, 1.0*0.5,
                new PIDObject(0.6/vN,0.002/vN,12.0/aN, 1),0.01*vN,
                new PIDObject(0.5/vNr,0,12.0/aNr, 1),0.01*vNr,
                false);

        mainJoystick.Y.whenPressed(new ThreadedCommand(profi, Chassis.getInstance()));

        mainJoystick.R1.whenPressed(new ThreeStageTesting.Starter());
        mainJoystick.R1.whenReleased(new StopShooter());

        mainJoystick.A.whenPressed(new Trench8BallAuto());
        mainJoystick.B.whenPressed(new StopShooter());

        mainJoystick.START.whenPressed(new CheckMaxLin(-0.5));
        mainJoystick.BACK.whenPressed(new CheckMaxRot(0.5));

        mainJoystick.L1.whileHeld(new InsertIntoShooter(0.5, 0.7));
        mainJoystick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

    }

    private void initOfficalButtons() {

        mainJoystick.A.whileHeld(new GoFetch());
        mainJoystick.A.whenReleased(new ArcadeDrive(mainJoystick));

        secondStick.R1.whenPressed(new FullyAutoThreeStage(2950, 0.49));
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new InsertIntoShooter(0.5, 0.7));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.7), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}