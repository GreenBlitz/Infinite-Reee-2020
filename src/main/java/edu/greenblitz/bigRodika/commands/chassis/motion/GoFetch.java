package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class GoFetch extends ChassisCommand {

    private Follow2DProfileCommand prof;
    private ThreadedCommand cmd;

    private State end;

    public GoFetch() {
        this(null);
    }

    public GoFetch(State endP) {
        this.end = endP;
    }

    @Override
    public void initialize() {
        VisionMaster.Algorithm.POWER_CELLS.setAsCurrent();
        List<State> locations = new ArrayList<>();
        locations.add(new State(0, 0, 0));


        if (end == null) {
            VisionLocation location = VisionMaster.getInstance().getVisionLocation();

            double ang;
            if (location.y == 0) {
                ang = 0;
            } else if (location.x == 0) {
                ang = Math.PI / 2;
            } else {
                ang = (Math.PI / 2) - Math.atan2(location.y * location.y - location.x * location.x, 2 * location.x * location.y);
            }

            locations.add(new State(location.x, location.y, ang));
        } else {
            locations.add(end);
        }

        ProfilingData data = RobotMap.Limbo2.Chassis.MotionData.POWER.get("0.5");
        double vN = data.getMaxLinearVelocity();
        double aN = data.getMaxLinearAccel();
        double vNr = data.getMaxAngularVelocity();
        double aNr = data.getMaxAngularAccel();
        prof = new Follow2DProfileCommand(locations,
                .001, 200,
                data,
                1.0,
                1.0 * 0.5, 1.0 * 0.5,
                new PIDObject(1.0 / vN, 0.002 / vN, 4.0 / aN, 1), 0.01 * vN,
                new PIDObject(0.6 / vNr, 0, 4.0 / aNr, 1), 0.01 * vNr,
                false, 0, Double.POSITIVE_INFINITY);
        cmd = new ThreadedCommand(prof);
        cmd.initialize();
        prof.setSendData(true);

    }

    @Override
    public void end(boolean interrupted) {
        cmd.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return cmd.isFinished();
    }

}