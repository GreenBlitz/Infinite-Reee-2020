package edu.greenblitz.bigRodika;

<<<<<<< HEAD
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
=======
import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.ApproachSwiftly;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
>>>>>>> dev
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
<<<<<<< HEAD
import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
=======
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.ThreeStageShoot;
>>>>>>> dev
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.TwoStageShoot;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
<<<<<<< HEAD
=======
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;

import java.util.ArrayList;
import java.util.List;
>>>>>>> dev

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


        mainJoystick.B.whenPressed(new ToggleExtender());
        mainJoystick.A.whileHeld(new RollByConstant(0.5));
//        mainJoystick.R1.whenPressed(new StopRoller());
//        mainJoystick.A.whenPressed(new ExtendRoller());
//        mainJoystick.B.whenPressed(new RetractRoller());
    }

    private void initOfficalButtons() {

        mainJoystick.A.whenPressed(new PreShoot());
        mainJoystick.A.whenReleased(new ArcadeDrive(mainJoystick));

        secondStick.R1.whenPressed(new FullyAutoThreeStage(2950, 0.49));
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new InsertIntoShooter(0.5, 0.7));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.7), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        mainJoystick.START.whenPressed(new CheckMaxRot(0.5));

    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() { return secondStick; }
}