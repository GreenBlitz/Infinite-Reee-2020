package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class GoFetch extends ChassisCommand {

    private State target;
    private Follow2DProfileCommand prof;
    private ThreadedCommand cmd;

    public GoFetch(State target) {
        this.target = target.clone();
        // should be gotten by the network tables
    }

    @Override
    public void initialize() {
        //VisionMaster.Algorithm.POWER_CELLS.setAsCurrent();
        List<State> locations = new ArrayList<>();
        locations.add(new State(0,0,0));
        locations.add(target);

//        VisionLocation location = VisionMaster.getInstance().getVisionLocation();
//
//        double ang;
//        if (location.y == 0){
//            ang = 0;
//        } else if (location.x == 0) {
//            ang = Math.PI / 2;
//        } else {
//            ang = (Math.PI / 2) - Math.atan2( location.y * location.y - location.x * location.x, 2 * location.x * location.y);
//        }



        ProfilingData data = RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5");
        prof = new Follow2DProfileCommand(locations,
                .001, 200,
                data,
                0.5, 1, 1,
                new PIDObject(0.1, 0, 0), .01 * data.getMaxLinearVelocity(),
                new PIDObject(0, 0, 0), .01 * data.getMaxAngularVelocity(),
                false);
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
