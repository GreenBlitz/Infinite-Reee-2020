package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.locazlier.LocalizerCommandRunner;
import edu.greenblitz.bigRodika.commands.complex.autonomous.FiveBallTrench;
import edu.greenblitz.bigRodika.commands.compressor.CompressorOn;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.turret.ResetEncoderWhenInSide;
import edu.greenblitz.bigRodika.subsystems.*;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.gears.Gear;
import edu.greenblitz.gblib.gears.GlobalGearContainer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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

        OI.getInstance();

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
//        Command shooterCommand = Shooter.getInstance().getCurrentCommand();
//        SmartDashboard.putString("Shooter::currentCommand", shooterCommand == null ? "" : shooterCommand.getName());
//        Command chassisCommand = Chassis.getInstance().getCurrentCommand();
//        SmartDashboard.putString("Chassis::currentCommand", chassisCommand == null ? "" : chassisCommand.getName());
    }

    @Override
    public void autonomousInit() {
        Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
        Shifter.getInstance().setShift(Gear.SPEED);
        VisionMaster.GameState.AUTONOMOUS.setAsCurrent();
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        new ResetEncoderWhenInSide().initialize();
        new LocalizerCommandRunner().schedule();
        new FiveBallTrench().schedule();
//        new ThreeBallNoVision().schedule();
//        new FiveBallTrenchSteal().schedule();
    }

    @Override
    public void teleopInit() {
        Shifter.getInstance().setShift(Gear.SPEED);
        CommandScheduler.getInstance().cancelAll();
        VisionMaster.GameState.TELEOP.setAsCurrent();
        Chassis.getInstance().toBrake();
        Chassis.getInstance().resetGyro();
        Chassis.getInstance().resetEncoders();

        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        Shifter.getInstance().setShift(Gear.SPEED);
        GlobalGearContainer.getInstance().setGear(Gear.SPEED);

        new ResetDome(-0.3).schedule();
        new CompressorOn();
//        new ResetEncoderWhenInFront().schedule();
        new StopShooter().schedule();

        SmartDashboard.putNumber("P", 0);
        SmartDashboard.putNumber("I", 0);
        SmartDashboard.putNumber("D", 0);
        SmartDashboard.putNumber("F", 0);

//        if (!DriverStation.getInstance().isFMSAttached()){
//            new CompressorOn().schedule();
//            new ResetEncoderWhenInSide().schedule();
//            Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
//        }
    }
}
