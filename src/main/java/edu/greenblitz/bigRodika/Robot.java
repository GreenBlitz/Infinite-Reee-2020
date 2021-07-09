package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.locazlier.LocalizerCommandRunner;
import edu.greenblitz.bigRodika.commands.climber.ClimbByTriggers;
import edu.greenblitz.bigRodika.commands.climber.HookByTriggers;
import edu.greenblitz.bigRodika.commands.complex.autonomous.FiveBallTrench;
import edu.greenblitz.bigRodika.commands.complex.autonomous.Trench8BallAuto;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRollerTeleop;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.turret.resets.ResetEncoderWhenInSide;
import edu.greenblitz.bigRodika.subsystems.*;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.greenblitz.bigRodika.utils.UARTCommunication;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.bigRodika.utils.WaitMiliSeconds;
import edu.greenblitz.gblib.gears.Gear;
import edu.greenblitz.gblib.gears.GlobalGearContainer;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.Localizer;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        CommandScheduler.getInstance().enable();

        DigitalInputMap.getInstance();

        Pneumatics.init();
        Intake.init();
        Shifter.init();
        Funnel.init();
        Shooter.init();
        Dome.init();
        Turret.init();
        Chassis.init(); // Must be last!

        UARTCommunication.getInstance().register();

        OI.getInstance();
        Turret.setDefaultCommand();

        VisionMaster.getInstance().register();

        new ResetEncoderWhenInSide().initialize();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        VisionMaster.GameState.DISABLED.setAsCurrent();
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void teleopPeriodic() {

        SmartDashboard.putNumber("RIGHT STICK X", OI.getInstance().getMainJoystick().getAxisValue(SmartJoystick.Axis.RIGHT_X));

        //SmartDashboard.putString("THING", Chassis.getInstance().getCurrentCommand().toString());
//        Command shooterCommand = Shooter.getInstance().getCurrentCommand();
//        SmartDashboard.putString("Shooter::currentCommand", shooterCommand == null ? "" : shooterCommand.getName());
//        Command chassisCommand = Chassis.getInstance().getCurrentCommand();
//        SmartDashboard.putString("Chassis::currentCommand", chassisCommand == null ? "" : chassisCommand.getName());
    }

    @Override
    public void autonomousInit() {
        Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
        //Shifter.getInstance().setShift(Gear.SPEED);
        VisionMaster.GameState.AUTONOMOUS.setAsCurrent();
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        new ResetEncoderWhenInSide().initialize();
        new LocalizerCommandRunner().schedule();
//        new Trench8BallAuto().schedule();
        new FiveBallTrench().schedule();
//        new ThreeBallNoVision().schedule();
//        new FiveBallTrenchSteal().schedule();
    }

    @Override
    public void teleopInit() {
        //Shifter.getInstance().setShift(Gear.SPEED);
        CommandScheduler.getInstance().cancelAll();
        VisionMaster.GameState.TELEOP.setAsCurrent();
        Chassis.getInstance().toBrake();
        Chassis.getInstance().resetGyro();
        Chassis.getInstance().resetEncoders();
        new LocalizerCommandRunner().schedule();

        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        //Shifter.getInstance().setShift(Gear.SPEED);
        GlobalGearContainer.getInstance().setGear(Gear.SPEED);

        new ResetDome(-0.3).schedule();
        new ResetEncoderWhenInSide();
        new ExtendRollerTeleop().schedule();

//        new StopShooter().schedule();


        /*
        if (!DriverStation.getInstance().isFMSAttached()){
//            new CompressorOn().schedule();
//            new ResetEncoderWhenInSide().schedule();
            new ClimbByTriggers(OI.getInstance().getMainJoystick(), OI.getInstance().getSideStick(), 0.4, 0.4).schedule();
            Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
        }*/

    }
}
