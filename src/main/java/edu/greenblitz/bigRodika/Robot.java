package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.locazlier.LocalizerCommandRunner;
import edu.greenblitz.bigRodika.commands.climber.ClimbByTriggers;
import edu.greenblitz.bigRodika.commands.climber.HookByTriggers;
import edu.greenblitz.bigRodika.commands.complex.autonomous.FiveBallTrench;
import edu.greenblitz.bigRodika.commands.complex.autonomous.Trench8BallAuto;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
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

import java.io.*;
import java.util.HashMap;

public class Robot extends TimedRobot {

    private double startTime;
    private boolean recordDriver = true;//will be false when we need to stop the recording.
    private HashMap<Double, HashMap<String, Double>> followDriverData;

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
        new ResetEncoderWhenInSide().schedule();
        new ExtendRoller().schedule();
//        new StopShooter().schedule();


        /*
        if (!DriverStation.getInstance().isFMSAttached()){
//            new CompressorOn().schedule();
//            new ResetEncoderWhenInSide().schedule();
            new ClimbByTriggers(OI.getInstance().getMainJoystick(), OI.getInstance().getSideStick(), 0.4, 0.4).schedule();
            Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
        }*/

    }

    @Override
    public void testInit() {
        startTime = System.currentTimeMillis() / 1000.0;
    }

    @Override
    public void testPeriodic() {
        if (OI.getInstance().getMainJoystick().START.get() && recordDriver) {
            recordDriver = false;

        }
        if (recordDriver) {
            followDriverData.put((System.currentTimeMillis() / 1000.0) - startTime, OI.getInstance().getMainJoystick().getButtonsOn());
        }
    }

    //todo: make sure the all is good
    private <T, K> void serializeHashMap(HashMap<T, K> myMap) {
        try {
            //when u run this code make sure to know the dest for the file.
            FileOutputStream myFileOutStream = new FileOutputStream("/src/main/resources/saveHashmap.txt");
            ObjectOutputStream myObjectOutStream = new ObjectOutputStream(myFileOutStream);
            myObjectOutStream.writeObject(myMap);//should check that writeObject will work recursively (we deal with HashMap in HashMap)
            //asaf said it will be find and it will work recursively
            myObjectOutStream.close();
            myFileOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //we copied those functions from gf"g, link:
    //https://www.geeksforgeeks.org/how-to-serialize-hashmap-in-java/
    private HashMap deserializeHashMap(String fileName) {
        try {
            FileInputStream fileInput = new FileInputStream("/src/main/resources/".concat(fileName).concat("txt"));
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            HashMap myMap = (HashMap) objectInput.readObject();
            objectInput.close();
            fileInput.close();
            return myMap;
        } catch (IOException obj1) {
            obj1.printStackTrace();
            return null;
        } catch (ClassNotFoundException obj2) {
            System.out.println("Class not found");
            obj2.printStackTrace();
            return null;
        }
    }


}
