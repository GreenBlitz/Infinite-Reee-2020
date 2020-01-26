package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.motion.GoFetch;
import edu.greenblitz.bigRodika.commands.chassis.HexAlign;
import edu.greenblitz.bigRodika.commands.chassis.PreShoot;
import edu.greenblitz.bigRodika.commands.chassis.TurnToVision;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.bigRodika.utils.VisionMaster;

import edu.greenblitz.gblib.hid.SmartJoystick;


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
        mainJoystick.X.whenPressed(new PreShoot());
        mainJoystick.Y.whenPressed(new HexAlign());
        mainJoystick.L3.whenPressed(new ToggleShift(Shifter.getInstance()));
        mainJoystick.B.whenPressed(new TurnToVision(VisionMaster.Algorithm.HEXAGON,RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularVelocity(),RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularAccel(),0.4));
        //mainJoystick.B.whenPressed(new TurnToAngle(
          //      0,5,1,2.1, 10, 0.4));
    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}