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
	private final String dataPlace = "/home/lvuser/command_recordings/ourRecord";

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
		//Shifter.getInstance().setShift(Gear.SPEED);
		CommandScheduler.getInstance().cancelAll();
		//VisionMaster.GameState.TELEOP.setAsCurrent();
		//Chassis.getInstance().toBrake();
		//Chassis.getInstance().resetGyro();
		//Chassis.getInstance().resetEncoders();
		//new LocalizerCommandRunner().schedule();

		//VisionMaster.Algorithm.HEXAGON.setAsCurrent();
		//Shifter.getInstance().setShift(Gear.SPEED);
		//GlobalGearContainer.getInstance().setGear(Gear.SPEED);

		//new ResetDome(-0.3).schedule();
		//new ResetEncoderWhenInSide().schedule();
		//new ExtendRoller().schedule();
		//new StopShooter().schedule();

        /*
        if (!DriverStation.getInstance().isFMSAttached()){
            new CompressorOn().schedule();
            new ResetEncoderWhenInSide().schedule();
            new ClimbByTriggers(OI.getInstance().getMainJoystick(), OI.getInstance().getSideStick(), 0.4, 0.4).schedule();
            Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
        }*/
	}

	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("RIGHT STICK X", OI.getInstance().getMainJoystick().getAxisValue(SmartJoystick.Axis.RIGHT_X));
		//SmartDotring("THING", Chassis.getInstance().getCurrentCommand().toString());
		//Command shooterCommand = Shooter.getInstance().getCurrentCommand();
		//SmartDashboard.putString("Shooter::currentCommand", shooterCommand == null ? "" : shooterCommand.getName());
		//Command chassisCommand = Chassis.getInstance().getCurrentCommand();
		//SmartDashboard.putString("Chassis::currentCommand", chassisCommand == null ? "" : chassisCommand.getName());
		//follow driver code is here:
//		collectFollowDriverData(false);
	}


	public void collectFollowDriverData(boolean isInTest) {
		if (isInTest) {
			if (OI.getInstance().getMainJoystick().START.get() && recordDriver) {
				recordDriver = false;
				serializeHashMap(followDriverData, dataPlace);
			}
			if (recordDriver) {
				followDriverData.put((System.currentTimeMillis() / 1000.0) - startTime, OI.getInstance().getMainJoystick().getButtonsOn());
			}
		} else {
//			OI.getInstance().setMainJoystick(new SmartJoystick(new VirtualJoystick(deserializeHashMap(dataPlace)))); //will work one day
		}
	}

	@Override
	public void autonomousInit() {
		Localizer.getInstance().reset(Chassis.getInstance().getLeftMeters(), Chassis.getInstance().getRightMeters());
		//Shifter.getInstance().setShift(Gear.SPEED);
		VisionMaster.GameState.AUTONOMOUS.setAsCurrent();
		VisionMaster.Algorithm.HEXAGON.setAsCurrent();
		// new ResetEncoderWhenInSide().initialize();
		new LocalizerCommandRunner().schedule();
		//new Trench8BallAuto().schedule();
		//new FiveBallTrench().schedule();
		//new ThreeBallNoVision().schedule();
		//new FiveBallTrenchSteal().schedule();
	}


	@Override
	public void testInit() {
		CommandScheduler.getInstance().cancelAll();
	}

	@Override
	public void testPeriodic() {
		SmartDashboard.putNumber("RIGHT STICK X", OI.getInstance().getMainJoystick().getAxisValue(SmartJoystick.Axis.RIGHT_X));
//		collectFollowDriverData(false);
	}

	//todo: make sure the all is good
	private <T, K> void serializeHashMap(HashMap<T, K> myMap, String file) {
		try {
			//when u run this code make sure to know the dest for the file.
			FileOutputStream myFileOutStream = new FileOutputStream(file);
			ObjectOutputStream myObjectOutStream = new ObjectOutputStream(myFileOutStream);
			myObjectOutStream.writeObject(myMap);//should check that writeObject will work recursively (we deal with HashMap in HashMap)
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
			FileInputStream fileInput = new FileInputStream(dataPlace);
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			HashMap<Double, HashMap<Integer, Double>> myMap = (HashMap<Double, HashMap<Integer, Double>>) objectInput.readObject();
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
