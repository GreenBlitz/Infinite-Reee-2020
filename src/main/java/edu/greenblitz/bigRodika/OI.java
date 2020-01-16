package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.motion.GoFetch;
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
//        mainJoystick.A.whenPressed(new CheckMaxRot(0.5));
        mainJoystick.R1.whenPressed(new GoFetch(new Point(0.5*0.6,0.5
                *0.6),Math.PI/2));
        mainJoystick.A.whenPressed(new GoFetch(new Point(1*0.6,1*0.6),Math.PI/2));
        mainJoystick.B.whenPressed(new GoFetch(new Point(1*0.6,2*0.6),Math.PI/2));
        mainJoystick.Y.whenPressed(new GoFetch(new Point(1*0.6,0.5*0.6),Math.PI/2));
        mainJoystick.X.whenPressed(new GoFetch(new Point(1.50*0.6,2*0.6),Math.PI/2));

    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}