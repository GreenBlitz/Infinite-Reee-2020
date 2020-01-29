package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.HexAlign;
import edu.greenblitz.bigRodika.commands.chassis.profiling.AdaptiveProfilingPursuitController;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
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
        mainJoystick.A.whenPressed(new CheckMaxRot(0.7));
        mainJoystick.X.whenPressed(new CheckMaxLin(0.7));

        ProfilingData data = RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.7");

        mainJoystick.Y.whenPressed(new ThreadedCommand(
                new AdaptiveProfilingPursuitController(() -> {
                    Point ref = new Point(0, 2);
                    return new State(ref);
                },
                        AdaptiveProfilingPursuitController.TargetMode.RELATIVE_TO_LOCALIZER, 0, data,
                        0.7,
                        new PIDObject(0.8/data.getMaxLinearVelocity(), 0, 25/data.getMaxLinearAccel()),
                        .01*data.getMaxLinearVelocity(),
                        new PIDObject(0.5/data.getMaxAngularVelocity(), 0, 0/data.getMaxAngularAccel()),
                        .01*data.getMaxAngularVelocity(),
                        false),

                Chassis.getInstance())
        );

        mainJoystick.B.whenPressed(new HexAlign());
        mainJoystick.L3.whenPressed(new ToggleShift());

    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}