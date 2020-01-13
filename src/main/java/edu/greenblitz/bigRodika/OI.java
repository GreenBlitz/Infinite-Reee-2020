package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.GoFetch;
import edu.greenblitz.bigRodika.commands.chassis.TurnToAngle;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.gblib.hid.SmartJoystick;
import org.greenblitz.motion.base.Point;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.BigRodika.Joystick.MAIN);
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
        mainJoystick.A.whenPressed(new CheckMaxRot(0.5));
        mainJoystick.B.whenPressed(new GoFetch(new Point(1,1)));
    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}