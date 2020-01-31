package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.shooter.FullShoot;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
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

        mainJoystick.X.whileHeld(new FullShoot(300));
        mainJoystick.X.whenReleased(new StopShooter());

        mainJoystick.A.whileHeld(new ShootByConstant(0.1));
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