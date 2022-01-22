package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.commands.chassis.driver.ArcadeDrive;
import edu.greenblitz.bigRodika.commands.chassis.locazlier.LocalizerCommandRunner;
import edu.greenblitz.bigRodika.subsystems.*;
import edu.greenblitz.bigRodika.utils.DigitalInputMap;
import edu.greenblitz.bigRodika.utils.UARTCommunication;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.bigRodika.utils.WaitMiliSeconds;
import edu.greenblitz.gblib.gears.Gear;
import edu.greenblitz.gblib.gears.GlobalGearContainer;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.greenblitz.gblib.hid.virtualHid.VirtualJoystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import jdk.dynalink.support.AbstractRelinkableCallSite;
import org.greenblitz.motion.Localizer;

import java.io.*;
import java.util.HashMap;

public class Robot extends TimedRobot {

	private double startTime;
	private boolean recordDriver = true;//will be false when we need to stop the recording.
	private HashMap<Double, HashMap<Integer, Double>> followDriverData = new HashMap<>();

	@Override
	public void robotInit() {
		CommandScheduler.getInstance().enable();

		DigitalInputMap.getInstance();

		//Pneumatics.init();
		//Intake.init();
		//Shifter.init();
		//Funnel.init();
		//Shooter.init();
		//Dome.init();
		//Turret.init();

//        UARTCommunication.getInstance().register();

		OI.getInstance();

//        VisionMaster.getInstance().register();
		Chassis.init(); // Must be last!

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
	public void teleopInit() {
		System.out.println("lets goooo + lets goooo + lets goooo + lets0 goooo + lets goooo + lets goooo + lets goooo +" +
				" lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo +" +
				" lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo +" +
				" lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo +" +
				" lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo +" +
				" lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo +" +
				" lets goooo + lets goooo + lets goooo + lets goooo + lets goooo + lets goooo +" +
				"" +
				deserializeHashMap("C:\\Users\\GreenBlitz User\\Desktop\\ourRecord"));
		//Shifter.getInstance().setShift(Gear.SPEED);
		CommandScheduler.getInstance().cancelAll();
//        VisionMaster.GameState.TELEOP.setAsCurrent();
		Chassis.getInstance().toBrake();
//        Chassis.getInstance().resetGyro();
//        Chassis.getInstance().resetEncoders();
//        new LocalizerCommandRunner().schedule();

//        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
		//Shifter.getInstance().setShift(Gear.SPEED);
//        GlobalGearContainer.getInstance().setGear(Gear.SPEED);

//        new ResetDome(-0.3).schedule();
//        new ResetEncoderWhenInSide().schedule();
//        new ExtendRoller().schedule();
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
	public void teleopPeriodic() {

		SmartDashboard.putNumber("RIGHT STICK X", OI.getInstance().getMainJoystick().getAxisValue(SmartJoystick.Axis.RIGHT_X));

		//SmartDotring("THING", Chassis.getInstance().getCurrentCommand().toString());
//        Command shooterCommand = Shooter.getInstance().getCurrentCommand();
//        SmartDashboard.putString("Shooter::currentCommand", shooterCommand == null ? "" : shooterCommand.getName());
//        Command chassisCommand = Chassis.getInstance().getCurrentCommand();
//        SmartDashboard.putString("Chassis::currentCommand", chassisCommand == null ? "" : chassisCommand.getName());
//		follow driver code is here:
		if (true) {
			if (OI.getInstance().getMainJoystick().START.get() && recordDriver) {
				recordDriver = false;
				serializeHashMap(followDriverData);
				System.out.println(followDriverData);
			}
			if (recordDriver) {
				followDriverData.put((System.currentTimeMillis() / 1000.0) - startTime, OI.getInstance().getMainJoystick().getButtonsOn());
			}
		} else {
			OI.getInstance().setMainJoystick(new SmartJoystick(new VirtualJoystick(deserializeHashMap("C:\\Users\\GreenBlitz User\\Desktop\\ourRecord"))));
		}
	}

	@Override
	public void autonomousInit() {
		Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
		//Shifter.getInstance().setShift(Gear.SPEED);
		VisionMaster.GameState.AUTONOMOUS.setAsCurrent();
		VisionMaster.Algorithm.HEXAGON.setAsCurrent();
//        new ResetEncoderWhenInSide().initialize();
		new LocalizerCommandRunner().schedule();
//        new Trench8BallAuto().schedule();
//        new FiveBallTrench().schedule();
//        new ThreeBallNoVision().schedule();
//        new FiveBallTrenchSteal().schedule();
	}


	@Override
	public void testInit() {
//		startTime = System.currentTimeMillis() / 1000.0;
//		CommandScheduler.getInstance().cancelAll();
//		Chassis.getInstance().toBrake();
		CommandScheduler.getInstance().cancelAll();
		Chassis.getInstance().toBrake();
	}

	@Override
	public void testPeriodic() {
		SmartDashboard.putNumber("RIGHT STICK X", OI.getInstance().getMainJoystick().getAxisValue(SmartJoystick.Axis.RIGHT_X));

		//new ArcadeDrive(OI.getInstance().getMainJoystick()).schedule();
//		System.out.println(Chassis.getInstance().getCurrentCommand().getName());
//		if (true) {
//			if (OI.getInstance().getMainJoystick().START.get() && recordDriver) {
//				recordDriver = false;
//				serializeHashMap(followDriverData);
//				System.out.println(followDriverData);
//			}
//			if (recordDriver) {
//				followDriverData.put((System.currentTimeMillis() / 1000.0) - startTime, OI.getInstance().getMainJoystick().getButtonsOn());
//			}
//		} else {
//			OI.getInstance().setMainJoystick(new SmartJoystick(new VirtualJoystick(deserializeHashMap("C:\\Users\\GreenBlitz User\\Desktop\\ourRecord"))));
//		}
	}

	//todo: make sure the all is good
	private <T, K> void serializeHashMap(HashMap<T, K> myMap) {
		try {
			//when u run this code make sure to know the dest for the file.
			FileOutputStream myFileOutStream = new FileOutputStream("/home/lvuser/command_recordings/ourRecord");
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


	@SuppressWarnings("unchecked")
	private HashMap<Double, HashMap<Integer, Double>> deserializeHashMap(String fileName) {
		try {
			System.out.println("Rapid Reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
			FileInputStream fileInput = new FileInputStream("C:\\Users\\GreenBlitz User\\IdeaProjects\\infinite-Reee-2020\\src\\main\\resources\\ourRecord");
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			HashMap<Double, HashMap<Integer, Double>> myMap = (HashMap<Double, HashMap<Integer, Double>>) objectInput.readObject();
			objectInput.close();
			fileInput.close();
			System.out.println(myMap);
			return myMap;
		} catch (IOException obj1) {
			System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111");
			obj1.printStackTrace();
			return null;
		} catch (ClassNotFoundException obj2) {
			System.out.println("222222222222222222222222222222222222222222222222222222222222222222222");
			System.out.println("Class not found");
			obj2.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		Robot r = new Robot();
		System.out.println(r.deserializeHashMap(""));

	}
}
