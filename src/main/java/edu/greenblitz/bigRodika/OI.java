package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.driver.WeakArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBalls;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.gblib.threading.ThreadedCommand;
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
        mainJoystick = new SmartJoystick(RobotMap.BigRodika.Joystick.MAIN, 0.08);
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

    private void initTestButtons(){

        // 0.6 = 3100 rpm

//        secondStick.R1.whenPressed(new ShootByConstant(0.6));
//        secondStick.R1.whenPressed(new SequentialCommandGroup(new ParallelRaceGroup(new WaitUntilShooterSpeedClose(3100, 100),
//                new ShootByConstant(1.0)),
//
//                new ShootByDashboard(3100)));
        secondStick.R1.whenPressed(new ShootBalls());
        secondStick.R1.whenReleased(new StopShooter());
        /*
        mainJoystick.A.whileHeld(new ShootByConstant(0.8));
        mainJoystick.A.whenReleased(new StopShooter());
        */
        secondStick.L1.whileHeld(new ParallelCommandGroup(new PushByConstant(0.6), new InsertByConstant(0.6)));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        mainJoystick.R3.whileHeld(new WeakArcadeDrive(secondStick, 0.2));

        List<State> test = new ArrayList<>();
        test.add(new State(0,0));
        test.add(new State(1, 1, Math.PI/2));

        mainJoystick.L3.whenPressed(new ThreadedCommand(new Follow2DProfileCommand(test, 0.001 ,1000,
                RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5"),
                0.5,1, 1,
        new PIDObject(0.1, 0, 0),0,new PIDObject(1, 0, 0.1),0,false), Chassis.getInstance()));

        secondStick.Y.whileHeld(new ParallelCommandGroup(new PushByConstant(-0.5), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        mainJoystick.A.whenPressed(new PreShoot());
        mainJoystick.B.whenPressed(new CheckMaxRot(0.5));

    }

    private void initOfficalButtons(){

        mainJoystick.A.whenPressed(new PreShoot());

        secondStick.R1.whenPressed(new ShootBalls());
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new ParallelCommandGroup(new PushByConstant(0.9), new InsertByConstant(0.8)));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}