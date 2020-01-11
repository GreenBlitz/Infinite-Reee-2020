package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.profiling.ActuatorLocation;
import org.greenblitz.motion.profiling.MotionProfile1D;
import org.greenblitz.motion.profiling.Profiler1D;

public class TurnExactAngle extends GBCommand {

    private ActuatorLocation end;
    private MotionProfile1D motionProfile;
    private double locP, velP;;
    private long t0;

    private boolean over;

    public TurnExactAngle(double angleToTurnDeg, double locP, double velP) {
        super(Chassis.getInstance());
        this.locP = locP;
        this.velP = velP;
        this.end = new ActuatorLocation(Math.toRadians(angleToTurnDeg), 0);
    }

    @Override
    public void initialize() {
        ActuatorLocation start = new ActuatorLocation(Chassis.getInstance().getAngle(),
                Chassis.getInstance().getAngularVelocityByGyro());

        this.motionProfile = Profiler1D.generateProfile(
                RobotMap.BigRodika.Chassis.MAX_ANG_V,
                RobotMap.BigRodika.Chassis.MAX_ANG_A,
                -2*RobotMap.BigRodika.Chassis.MAX_ANG_A,
                0, start, end);

        t0 = System.currentTimeMillis();
        over = false;
    }

    @Override
    public void execute() {

        double timePassed = (System.currentTimeMillis() - t0) / 1000.0;

        if (motionProfile.isOver(timePassed)){
            Chassis.getInstance().moveMotors(0,0);
            over = true;
            return;
        }

        double velocity = motionProfile.getVelocity(timePassed);
        double perWheelVel = velocity * Chassis.getInstance().getWheelDistance() / 2.0;
        double accel = motionProfile.getAcceleration(timePassed);
        double location = motionProfile.getLocation(timePassed);

        double ff = velocity/RobotMap.BigRodika.Chassis.MAX_ANG_V  + accel/ RobotMap.BigRodika.Chassis.MAX_ANG_A;
        double locPVal = locP * Position.normalizeAngle(location - Chassis.getInstance().getAngle());
        double velPLeft = velP * (perWheelVel - Chassis.getInstance().getLeftRate());
        double velPRight = velP * (-perWheelVel - Chassis.getInstance().getLeftRate());

        Chassis.getInstance().moveMotors(ff + locPVal + velPLeft, -(ff + locPVal + velPRight));
    }

    @Override
    public void end(boolean interrupted) {
        SmartDashboard.putNumber("Final Error", Math.toDegrees(Chassis.getInstance().getAngle() - end.getX()));
    }

    @Override
    public boolean isFinished() {
        return over;
    }
}
