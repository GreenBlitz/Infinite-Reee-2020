package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class GoFetch extends GBCommand {

    private Follow2DProfileCommand prof;

    public GoFetch(Point target, double angle) {
        super(Chassis.getInstance());
        // should be gotten by the network tables

        List<State> locations = new ArrayList<>();
        locations.add(new State(0, 0, 0));
<<<<<<< HEAD:src/main/java/edu/greenblitz/bigRodika/commands/chassis/GoFetch.java
        locations.add(new State(target.getX(), target.getY(), angle, 0, 0));

        ProfilingData data =  RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.7");

        prof = new Follow2DProfileCommand(locations,
                .001, 200,
                data,
                0.7, 1, 1,
                new PIDObject(0*data.getMaxLinearVelocity(), 0, 0*data.getMaxLinearVelocity()), 0.01*data.getMaxLinearVelocity(),
                new PIDObject(0*data.getMaxAngularVelocity(), 0, 0*data.getMaxAngularAccel()), 0.01*data.getMaxAngularVelocity(),
                false);
        prof.setSendData(true);
=======
        locations.add(new State(target.getX(), target.getY(), 0, 0, 0));
       // System.out.println("locations ");
        profile2D = ChassisProfiler2D.generateProfile(locations, JMP, MAX_LIN_V, MAX_ANG_V, MAX_LIN_A, MAX_ANG_A, 0, 1, 800);
       // System.out.println("profile2D");
        follower2D = null;
        //System.out.println("follower2D");
        System.out.println(profile2D.getTEnd());
>>>>>>> e9d96835e6998acaa814b8aca397bf7da50d9db8:src/main/java/edu/greenblitz/bigRodika/commands/chassis/motion/GoFetch.java
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        new ThreadedCommand(prof, Chassis.getInstance()).schedule();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
