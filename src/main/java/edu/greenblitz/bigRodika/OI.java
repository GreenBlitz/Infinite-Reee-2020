package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.motion.ChainFetch;
import edu.greenblitz.bigRodika.commands.chassis.motion.DumbAlign;
import edu.greenblitz.bigRodika.commands.chassis.motion.PreShoot;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
import edu.greenblitz.bigRodika.commands.intake.extender.RetractRoller;
import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;
import edu.greenblitz.bigRodika.commands.shifter.ToPower;
import edu.greenblitz.bigRodika.commands.shifter.ToSpeed;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.*;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN,
                RobotMap.Limbo2.Joystick.MAIN_DEADZONE);
        secondStick = new SmartJoystick(RobotMap.Limbo2.Joystick.SIDE,
                RobotMap.Limbo2.Joystick.SIDE_DEADZONE);

//        initTestButtons();
        initOfficalButtons();
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons() {

        //mainJoystick.R1.whileHeld(new ChainFetch(5, mainJoystick));
        //mainJoystick.R1.whenReleased(new ArcadeDrive(mainJoystick));
        mainJoystick.B.whenPressed(new DumbAlign(5.0, 0.1, 0.5));
        mainJoystick.X.whenPressed(new ResetEncoderWhenInFront());

        secondStick.L3.whenPressed(new ResetEncoderWhenInFront());
        secondStick.A.whenPressed(new TurretByVision(VisionMaster.Algorithm.HEXAGON));
        secondStick.A.whenReleased(new StopTurret());

        secondStick.POV_UP.whenPressed(new DomeMoveByConstant(0.3));
        secondStick.POV_UP.whenReleased(new DomeMoveByConstant(0));
        secondStick.POV_DOWN.whenPressed(new DomeMoveByConstant(-0.3));
        secondStick.POV_DOWN.whenReleased(new DomeMoveByConstant(0));


        secondStick.START.whenPressed(new MoveTurretByConstant(0.3));
        secondStick.START.whenReleased(new StopTurret());

        secondStick.BACK.whenPressed(new MoveTurretByConstant(-0.3));
        secondStick.BACK.whenReleased(new StopTurret());

    }

    private void initOfficalButtons() {

//        mainJoystick.R1.whileHeld(new ChainFetch(5, mainJoystick));
//        mainJoystick.R1.whenReleased(new ArcadeDrive(mainJoystick));

        mainJoystick.A.whileHeld(new PreShoot(new DumbAlign(6.3, .1, .5)));

//        mainJoystick.L1.whenReleased(new ToggleShift());

        mainJoystick.B.whenPressed(new TurretByVision(VisionMaster.Algorithm.HEXAGON));
        mainJoystick.B.whenReleased(new StopTurret());

        mainJoystick.X.whenPressed(new ParallelCommandGroup(
                new ResetDome(-0.3), new ExtendRoller()
        ));

        mainJoystick.START.whenPressed(new ToSpeed());
        mainJoystick.BACK.whenPressed(new ToPower());

        mainJoystick.POV_UP.whenPressed(new DomeApproachSwiftly(RobotMap.Limbo2.Dome.DOME.get(6.3)));

        // ---------------------------------------------------------------

        secondStick.R1.whenPressed(new FullyAutoThreeStage(3700, 0.65));
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.L1.whileHeld(new InsertIntoShooter(1, 0.5, 0.1));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondStick.Y.whileHeld(new
                ParallelCommandGroup(new PushByConstant(-0.3), new InsertByConstant(-0.6)));
        secondStick.Y.whenReleased(new ParallelCommandGroup(new StopPusher(), new StopInserter()));

        secondStick.B.whenPressed(new ToggleExtender());
        secondStick.A.whileHeld(new RollByConstant(1.0));

        secondStick.START.whenPressed(new ParallelCommandGroup(
                new TurretApproachSwiftlyRadians(-Math.PI),
                new DomeApproachSwiftly(0.11)
        ));

        mainJoystick.X.whenPressed(new TurretByVision(VisionMaster.Algorithm.HEXAGON));
        mainJoystick.X.whenReleased(new StopTurret());

        secondStick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

        secondStick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));

        secondStick.POV_LEFT.whileHeld(new MoveTurretByConstant(-0.2));

        secondStick.POV_RIGHT.whileHeld(new MoveTurretByConstant(0.2));

        secondStick.BACK.whenPressed(new ResetDome(-0.3));
    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}