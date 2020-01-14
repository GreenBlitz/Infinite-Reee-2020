package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.TurnToAngle;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.gblib.hid.SmartJoystick;

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
        mainJoystick.A.whenPressed(new CheckMaxRot(0.3));
        mainJoystick.X.whenPressed(new CheckMaxLin(0.3));
        mainJoystick.B.whenPressed(new TurnToAngle(
                15,10,2,4.8, 8.5, 0.5));
    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}