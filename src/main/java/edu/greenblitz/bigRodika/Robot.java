package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.locazlier.LocalizerCommandRunner;
import edu.greenblitz.bigRodika.subsystems.*;

import edu.greenblitz.bigRodika.subsystems.Chassis;

import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.greenblitz.motion.Localizer;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        CommandScheduler.getInstance().enable();

//        Pneumatics.init();
//        Shifter.init();
        Funnel.init();
        Shooter.init();
        Chassis.init(); // Must be last!

        OI.getInstance();

        SmartDashboard.putNumber("p", 0);
        SmartDashboard.putNumber("i", 0);
        SmartDashboard.putNumber("d", 0);
        SmartDashboard.putNumber("f", 0);

        VisionMaster.getInstance();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
//        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void teleopPeriodic() {
        Command shooterCommand = Shooter.getInstance().getCurrentCommand();
        SmartDashboard.putString("Shooter::currentCommand", shooterCommand == null ? "" : shooterCommand.getName());
        Command chassisCommand = Chassis.getInstance().getCurrentCommand();
        SmartDashboard.putString("Chassis::currentCommand", chassisCommand == null ? "" : chassisCommand.getName());
    }

    @Override
    public void teleopInit() {
        Chassis.getInstance().toBrake();
        Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
        Chassis.getInstance().resetEncoders();
        Shooter.getInstance().resetEncoder();

        new LocalizerCommandRunner().schedule();
    }
}
