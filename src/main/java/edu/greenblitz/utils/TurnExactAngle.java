package edu.greenblitz.utils;

import edu.greenblitz.bigRodika.Robot;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import org.greenblitz.motion.profiling.ActuatorLocation;
import org.greenblitz.motion.profiling.MotionProfile1D;
import org.greenblitz.motion.profiling.Profiler1D;

public class TurnExactAngle {
    private double turn, leftInitialMeters, rightInitialMeters;
    private ActuatorLocation beginning, end;
    private MotionProfile1D motionProfile;

    private static final double MAX_V = 2, MAX_A = 1, MIN_A = 0.2, T_START = 0;

    public TurnExactAngle(float turn, float init_v, float end_v) {
        this.turn = Chassis.getInstance().getAngle() + turn;
        this.beginning = new ActuatorLocation(Chassis.getInstance().getAngle(), end_v);
        this.end = new ActuatorLocation(this.turn, end_v);
        this.motionProfile = Profiler1D.generateProfile(MAX_V , MAX_A, MIN_A, T_START, beginning, end);
    }

}
