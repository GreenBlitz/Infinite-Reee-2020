package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxLin;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.shooter.FullShoot;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.bigRodika.commands.chassis.HexAlign;
import edu.greenblitz.bigRodika.commands.chassis.PreShoot;

import edu.greenblitz.bigRodika.commands.chassis.test.CheckMaxRot;
import edu.greenblitz.bigRodika.commands.chassis.turns.TurnToAngle;
import edu.greenblitz.bigRodika.commands.chassis.turns.TurnToVision;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
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
        /*
        mainJoystick.X.whileHeld(new FullShoot(3000));
        mainJoystick.X.whenReleased(new StopShooter());
        /*
        mainJoystick.A.whileHeld(new ShootByConstant(0.8));
        mainJoystick.A.whenReleased(new StopShooter());
        */
        mainJoystick.A.whileHeld(new PushByConstant(0.5));
        mainJoystick.A.whenReleased(new StopPusher());

        mainJoystick.Y.whileHeld(new InsertByConstant(0.8));
        mainJoystick.Y.whenReleased(new StopInserter());


        /*
        mainJoystick.A.whenPressed(new CheckMaxRot(0.5));
        mainJoystick.START.whenPressed(new CheckMaxLin(0.5));
        mainJoystick.X.whenPressed(new PreShoot());
        mainJoystick.Y.whenPressed(new HexAlign());
        mainJoystick.R1.whenPressed(new TurnToAngle(90, 5, 1, 2.1, 10, 0.4, false));
        */
        mainJoystick.B.whenPressed(new TurnToVision(VisionMaster.Algorithm.HEXAGON,RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularVelocity(),RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5").getMaxAngularAccel(),0.5));
        mainJoystick.R1.whenPressed(new PreShoot());
        //mainJoystick.B.whenPressed(new TurnToAngle(
        //      0,5,1,2.1, 10, 0.4));

    }

    private void initOfficalButtons(){

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}