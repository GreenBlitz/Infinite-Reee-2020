package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.base.Vector2D;
import org.greenblitz.motion.profiling.ChassisProfiler2D;
import org.greenblitz.motion.profiling.MotionProfile2D;
import org.greenblitz.motion.profiling.followers.PidFollower2D;

import java.util.ArrayList;
import java.util.List;

public class GoFetch extends GBCommand {
    private final double MAX_LIN_V = 1, MAX_ANG_V = 0.25, MAX_LIN_A = 1, MAX_ANG_A = 0.125, JMP = 0.0001;
    private Point target; // should be gotten by the network tables
    private MotionProfile2D profile2D;
    private PidFollower2D follower2D;
    private double t0;

    public GoFetch(Point target) {
        super(Chassis.getInstance());
        System.out.println("Ctor");
        this.target = target;
        System.out.println("got a target (1,1) ");
        List<State> locations = new ArrayList<>();
        System.out.println("crested a locations list");
        locations.add(new State(0, 0, 0));
        locations.add(new State(target.getX(), target.getY(), 0, 0, 0));
        System.out.println("added the locations to the list");
        profile2D = ChassisProfiler2D.generateProfile(locations, JMP, MAX_LIN_V, MAX_ANG_V, MAX_LIN_A, MAX_ANG_A, 0, 1, 800);
        follower2D = new PidFollower2D(1,1,1,1, RobotMap.BigRodika.Chassis.WHEEL_DIST, profile2D);
        System.out.println("generated a profile");
    }

    @Override
    public void initialize() {
        System.out.println("init");
        t0 = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        System.out.println("execute");
        double currT = (t0 - System.currentTimeMillis()) / 1000.0;
        Vector2D v = follower2D.run(Chassis.getInstance().getLeftRate(), Chassis.getInstance().getRightRate(), currT);
        Chassis.getInstance().tankDrive(v.getX(), v.getY());
        System.out.println("following");
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
