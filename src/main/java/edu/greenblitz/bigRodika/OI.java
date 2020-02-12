package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.driver.DriveUntilVision;
import edu.greenblitz.bigRodika.commands.chassis.motion.ChainFetch;
import edu.greenblitz.bigRodika.commands.chassis.motion.GoFetch;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.test.ThreeStageTesting;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;

import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN, 0.08);
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

        mainJoystick.R1.whenPressed(new ThreeStageTesting.Starter());
        mainJoystick.R1.whenReleased(new StopShooter());

        mainJoystick.L1.whileHeld(new InsertIntoShooter(0.5, 0.7));
        mainJoystick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

    }

    private void initOfficalButtons() {

//        mainJoystick.A.whileHeld(new GoFetch());
        mainJoystick.A.whenReleased(new ArcadeDrive(mainJoystick));

        secondStick.R1.whenPressed(new FullyAutoThreeStage(2950, 0.49));
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new InsertIntoShooter(0.5, 0.7));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.7), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}