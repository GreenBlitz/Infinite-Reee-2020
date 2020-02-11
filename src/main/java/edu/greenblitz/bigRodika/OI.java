package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.TwoStageShoot;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

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

    private void initTestButtons() {

//        mainJoystick.R1.whenPressed(new FullyAutoThreeStage(2800, 0.48));
//        mainJoystick.R1.whenReleased(new ParallelCommandGroup(
//                new StopShooter(), new StopInserter(), new StopPusher()));
//
//        mainJoystick.A.whenPressed(new PreShoot());

        mainJoystick.B.whenPressed(new ToggleExtender());
        mainJoystick.A.whileHeld(new RollByConstant(0.5));
//        mainJoystick.R1.whenPressed(new StopRoller());
//        mainJoystick.A.whenPressed(new ExtendRoller());
//        mainJoystick.B.whenPressed(new RetractRoller());
    }

    private void initOfficalButtons() {

        mainJoystick.A.whenPressed(new PreShoot());

        secondStick.R1.whenPressed(new TwoStageShoot());
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new ParallelCommandGroup(new PushByConstant(0.9), new InsertByConstant(0.8)));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }
}