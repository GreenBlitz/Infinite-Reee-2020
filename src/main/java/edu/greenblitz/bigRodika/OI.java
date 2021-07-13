package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.complex.multisystem.SequentialFullShoot;
import edu.greenblitz.bigRodika.commands.complex.multisystem.ShootAdjesant;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.DomeMoveByConstant;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.BetterFunnelCommand;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.inserter.StopInserter;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByDifferentConstants;
import edu.greenblitz.bigRodika.commands.funnel.pusher.StopPusher;
import edu.greenblitz.bigRodika.commands.intake.extender.ToggleExtender;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.commands.intake.roller.StopRoller;
import edu.greenblitz.bigRodika.commands.shifter.ToPower;
import edu.greenblitz.bigRodika.commands.shifter.ToSpeed;
import edu.greenblitz.bigRodika.commands.shifter.ToggleShift;
import edu.greenblitz.bigRodika.commands.shooter.FullShoot;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.ThreeStageShoot;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.commands.turret.resets.UnsafeResetTurret;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    public static final boolean DEBUG = false;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN,
                RobotMap.Limbo2.Joystick.MAIN_DEADZONE);
        secondStick = new SmartJoystick(RobotMap.Limbo2.Joystick.SIDE,
                RobotMap.Limbo2.Joystick.SIDE_DEADZONE);

        if (DEBUG) {
            initTestButtons();
        } else {
            initOfficialButtons();
        }
    }


    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons() {
//       mainJoystick.A.whileHeld(new TurretByVision(VisionMaster.Algorithm.HEXAGON));
//
        mainJoystick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

        mainJoystick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));
//
        mainJoystick.POV_LEFT.whileHeld(new MoveTurretByConstant(0.2));

        mainJoystick.POV_RIGHT.whileHeld(new MoveTurretByConstant(-0.2));
//
//        secondStick.B.whileHeld(new ParallelCommandGroup(new PushByDifferentConstants(0.6, 0.2), new InsertByConstant(0.6)));
//
//        secondStick.Y.whenPressed(new StopShooter());
//
        mainJoystick.L1.whenPressed(new ToggleExtender());

        Supplier<Double> visionDist = new Supplier<Double>() {
            @Override
            public Double get() {
                return VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
            }
        };

        //mainJoystick.A.whileHel
        // d(new FullShoot(visionDist));
//        mainJoystick.B.whenPressed(new ParallelCommandGroup(
//                new StopShooter(),

//                new ResetDome()
//        ));

//        mainJoystick.A.whileHeld(new BetterFunnelCommand(1906));
//        mainJoystick.A.whileHeld(new TurretByVision(VisionMaster.Algorithm.HEXAGON));
//        mainJoystick.B.whileHeld(new DomeApproachSwiftly(800));
        mainJoystick.X.whileHeld(new FullyAutoThreeStage(1906));
        mainJoystick.R1
                .whileHeld(new InsertIntoShooter(0.5, 0.5, 0.5));

        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, -1));

        mainJoystick.A.whenPressed(new DomeApproachSwiftly(2300));

        mainJoystick.Y.whenPressed(new StopShooter());
    }

    private void initOfficialButtons() {

        mainJoystick.L1.whenReleased(new ToggleShift());
        
        mainJoystick.START.whenPressed(new ToSpeed());
        mainJoystick.BACK.whenPressed(new ToPower());

        // --------------------SECOND STICK---------------------------------

        secondStick.L1.whileHeld(new BetterFunnelCommand(2062));
        secondStick.L1.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondStick.Y.whileHeld(new SequentialFullShoot());
        secondStick.Y.whenReleased(new StopShooter());

//        secondStick.A.whenPressed(new StopShooter());

        secondStick.B.whenPressed(new ToggleExtender());

        secondStick.R1.whenPressed(new FullyAutoThreeStage(2062)); // 1650
        secondStick.R1.whenReleased(new StopShooter());

        secondStick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

        secondStick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));

        secondStick.POV_LEFT.whileHeld(new MoveTurretByConstant(-0.35));

        secondStick.POV_RIGHT.whileHeld(new MoveTurretByConstant(0.35));

        secondStick.BACK.whenPressed(new ResetDome(-0.3));

        secondStick.X.whileHeld(new InsertIntoShooter(-1.0, -0.8, 0.6));
        secondStick.X.whenReleased(new ParallelCommandGroup(new StopPusher(),
                new StopInserter(), new StopRoller()));

        secondStick.R3.whenPressed(new GBCommand() {
            @Override
            public void initialize() {
                CommandScheduler.getInstance().cancelAll();

                new StopInserter().schedule();
                new StopPusher().schedule();
                new StopRoller().schedule();
                new StopShooter().schedule();

                Chassis.getInstance().moveMotors(0, 0);
            }
        }); // Fake disable command to make robot not die but still operating for the rest of the game after.
    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }
}