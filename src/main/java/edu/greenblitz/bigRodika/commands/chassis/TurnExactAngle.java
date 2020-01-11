package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.Robot;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.profiling.ActuatorLocation;
import org.greenblitz.motion.profiling.MotionProfile1D;
import org.greenblitz.motion.profiling.Profiler1D;

public class TurnExactAngle extends GBCommand {

    private double leftInitialMeters, rightInitialMeters;
    private ActuatorLocation start, end;
    private MotionProfile1D motionProfile;

    public TurnExactAngle(double angleToTurnDeg) {
        super(Chassis.getInstance());
        this.end = new ActuatorLocation(Math.toRadians(angleToTurnDeg), 0);
    }

    @Override
    public void initialize() {
        this.start = new ActuatorLocation(Chassis.getInstance().getAngle(),
                Chassis.getInstance().getAngularVelocityByGyro());
        this.motionProfile = Profiler1D.generateProfile(
                RobotMap.BigRodika.Chassis.MAX_ANG_V,
                RobotMap.BigRodika.Chassis.MAX_ANG_A,
                -2*RobotMap.BigRodika.Chassis.MAX_ANG_A,
                0, start, end);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
