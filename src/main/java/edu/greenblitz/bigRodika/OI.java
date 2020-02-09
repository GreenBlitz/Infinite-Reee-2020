package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.driver.WeakArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ThreeStageShoot;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.TwoStageShoot;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.BigRodika.Joystick.MAIN, 0.08);
        secondStick = new SmartJoystick(1, 0.05);
//        initTestButtons();
        initOfficalButtons();
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons(){

        mainJoystick.R1.whenPressed(new FullyAutoThreeStage(2950, 0.49));
        mainJoystick.R1.whenReleased(new ParallelCommandGroup(
                new StopShooter(), new StopInserter(), new StopPusher()));

        mainJoystick.A.whenPressed(new PreShoot());

        mainJoystick.Y.whenPressed(new CheckMaxRot(0.5));

        List<State> path = new ArrayList<>();
        path.add(new State(0, 0, 0));
        path.add(new State(1, 1, Math.PI/2));
        ProfilingData data = RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5");
        double vN = data.getMaxLinearVelocity();
        double aN = data.getMaxLinearAccel();
        double vNr = data.getMaxAngularVelocity();
        double aNr = data.getMaxAngularAccel();
        mainJoystick.B.whenPressed(new ThreadedCommand(
                new Follow2DProfileCommand(path, 0.001, 400, data, 1.0,
                        0.47, 0.4, // 0.575
                        new PIDObject(0.6/vN,0.004/vN,10.0/aN, 1),0.01*vN,
                        new PIDObject(0.2/vNr,0,10.0/aNr, 1),0.01*vNr,
                        false)
                ,
                Chassis.getInstance()));

    }

    private void initOfficalButtons(){

        mainJoystick.A.whenPressed(new PreShoot());
        mainJoystick.A.whenReleased(new ArcadeDrive(mainJoystick));

        secondStick.R1.whenPressed(new ThreeStageShoot(2950, 0.49));
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new
                ParallelCommandGroup(new PushByConstant(0.7), new InsertByConstant(0.6)));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.7), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        mainJoystick.START.whenPressed(new CheckMaxRot(0.5));

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}