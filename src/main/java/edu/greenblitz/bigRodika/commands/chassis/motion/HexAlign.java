package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.gears.Gear;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.profiling.ProfilingConfiguration;

import java.util.ArrayList;
import java.util.List;

public class HexAlign extends GBCommand {

	private Follow2DProfileCommand prof;
	private ThreadedCommand cmd;
	private ProfilingConfiguration config;
	private double k;
	private double r;
	private int profileAngleVsGyroInverted = -1;
	private int gyroInverted = 1;
	private Point globHexPos;
	private boolean fucked = false;
	private double driveTolerance;
	private double tolerance;
	private List<Double> radsAndCritPoints;//crit point - radius - crit - radius - crit .... - radius
	private Position globalEnd;
	private double maxPower;


	public HexAlign(double r, double k, double driveTolerance, double tolerance, double maxPower, ProfilingConfiguration config) {
		super();
		this.k = k;
		this.r = r;
		this.driveTolerance = driveTolerance;
		this.tolerance = tolerance;
		this.maxPower = maxPower;
		this.config = config;
	}

	public HexAlign(List<Double> radsAndCritPoints, double k, double driveTolerance, double tolerance, double maxPower, ProfilingConfiguration config) {
		super();
		this.radsAndCritPoints = radsAndCritPoints;
		this.k = k;
		this.tolerance = tolerance;
		this.driveTolerance = driveTolerance;
		this.maxPower = maxPower;
		this.config = config;
	}

	public HexAlign(double r, double k, double driveTolerance, double tolerance, double maxPower) {
		this(r, k, driveTolerance, tolerance, maxPower, RobotMap.Limbo2.Chassis.MotionData.CONFIG);
	}

	public HexAlign(List<Double> radsAndCritPoints, double k, double driveTolerance, double tolerance, double maxPower) {
		this(radsAndCritPoints, k, driveTolerance, tolerance, maxPower, RobotMap.Limbo2.Chassis.MotionData.CONFIG);
	}

	public Point getHexPos() {
		return globHexPos;
	}

	private Gear gearBefore;

	@Override
	public void initialize() {

//        gearBefore = Shifter.getInstance().getCurrentGear();
//        Shifter.getInstance().setShift(Gear.POWER);

//		double absAng = gyroInverted * (Chassis.getInstance().getAngle());// + RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET);

//		State startState = new State(0, 0, profileAngleVsGyroInverted * absAng);
		VisionMaster.Algorithm.HEXAGON.setAsCurrent();
		VisionLocation location = VisionMaster.getInstance().getVisionLocation();
		SmartDashboard.putString("Vision Location", location.toString());

		if (!VisionMaster.getInstance().isLastDataValid()) {
			fucked = true;
			return;
		}


		double cam_y = RobotMap.Limbo2.Chassis.VISION_CAM_Y_DIST_CENTER;


	}


	@Override
	public void execute() {
	}

	@Override
	public void end(boolean interupted) {
		if (!fucked) {
			cmd.stop();
//			SmartDashboard.putString("HexAlign error", new Position(Point.subtract(Chassis.getInstance().getLocation(), globalEnd), Chassis.getInstance().getAngle() - globalEnd.getAngle()).toString());
			//Shifter.getInstance().setShift(gearBefore);
		}
		cmd = null;
		prof = null;
	}

	@Override
	public boolean isFinished() {
		return fucked || cmd.isFinished();
	}
}