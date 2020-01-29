package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.HexAlign;
import edu.greenblitz.bigRodika.commands.chassis.profiling.AdaptiveProfilingPursuitController;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;


public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.BigRodika.Joystick.MAIN, 0.08);
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

        mainJoystick.A.whileHeld(new ShootByConstant(0.5));
        mainJoystick.A.whenReleased(new StopShooter());

        mainJoystick.B.whileHeld(new PushByConstant(0.5));
        mainJoystick.B.whenReleased(new StopPusher());

        mainJoystick.Y.whileHeld(new InsertByConstant(0.5));
        mainJoystick.Y.whenReleased(new StopInserter());

    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}