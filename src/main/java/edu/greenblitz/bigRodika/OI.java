package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.motion.GoFetch;
import edu.greenblitz.bigRodika.commands.chassis.HexAlign;
import edu.greenblitz.bigRodika.commands.chassis.TurnToVision;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.gblib.hid.SmartJoystick;
import org.greenblitz.motion.base.Point;


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
        mainJoystick.Y.whenPressed(new HexAlign());
        mainJoystick.B.whenPressed(new TurnToVision());
        mainJoystick.L3.whenPressed(new ToggleShift(Shifter.getInstance()));
    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}