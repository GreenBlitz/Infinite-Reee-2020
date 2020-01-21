package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.LocalizerCommand;
import edu.greenblitz.bigRodika.commands.chassis.ThreadedCommandLocalizer;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.app.Localizer;
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
        locations.add(new State(target.getX(), target.getY(), angle, 0, 0));

        ProfilingData data =  RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.7");
        prof = new Follow2DProfileCommand(locations,
                .001, 200,
                data,
                0.7, 1, 1,
                new PIDObject(0.8/data.getMaxLinearVelocity(), 0, 25/data.getMaxLinearAccel()), .01*data.getMaxLinearVelocity(),
                new PIDObject(0.5/data.getMaxAngularVelocity(), 0, 0/data.getMaxAngularAccel()), .01*data.getMaxAngularVelocity(),
                false);
        prof.setSendData(true);

    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        new ThreadedCommandLocalizer(prof, Chassis.getInstance()).schedule();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
