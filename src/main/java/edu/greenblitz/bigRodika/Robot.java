package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.LocalizerCommandRunner;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Pneumatics;
import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        CommandScheduler.getInstance().enable();

        Pneumatics.init();
        Shifter.init();
        Chassis.init(); // Must be last!

        OI.getInstance();


//        VisionMaster.getInstance();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
//        VisionMaster.getInstance().update();
    }

    @Override
    public void teleopInit() {
        Chassis.getInstance().toBrake();

        new LocalizerCommandRunner().schedule();
//        new ArcadeDrive(Chassis.getInstance(), OI.getInstance().getMainJoystick()).schedule();

    }
}
