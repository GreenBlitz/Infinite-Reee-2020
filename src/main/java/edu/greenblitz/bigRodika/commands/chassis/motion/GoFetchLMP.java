package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class GoFetchLMP extends GBCommand {
    private final double JMP = 0.001;
    private double t0;
    private boolean hasInitByVision = false;
    private Follow2DProfileCommand prof;
    private ThreadedCommand cmd;

    public GoFetchLMP(){
        super(Chassis.getInstance());
        t0 = System.currentTimeMillis();
    }

    private boolean initLocationByVision(){
        VisionLocation location = VisionMaster.getInstance().getVisionLocation();
        if(location.isValid()){
            List<State> locations = new ArrayList<>();
            locations.add(new State(0, 0, 0));
            hasInitByVision = true;
            double ang;
            if (location.y == 0){
                ang = 0;
            } else if (location.x == 0) {
                ang = Math.PI / 2;
            } else {
                ang = (Math.PI / 2) - Math.atan2( location.y * location.y - location.x * location.x, 2 * location.x * location.y);
            }
            locations.add(new State(location.x, location.y, ang));
            ProfilingData data = RobotMap.Limbo2.Chassis.MotionData.POWER.get("0.5");
            prof = new Follow2DProfileCommand(locations,
                    .001, 200,
                    data,
                    0.5, 1, 1,
                    new PIDObject(0.8 / data.getMaxLinearVelocity(), 0, 25 / data.getMaxLinearAccel()), .01 * data.getMaxLinearVelocity(),
                    new PIDObject(0.5 /
                            data.getMaxAngularVelocity(), 0, 0 / data.getMaxAngularAccel()), .01 * data.getMaxAngularVelocity(),
                    false);
            cmd = new ThreadedCommand(prof);
            cmd.initialize();
            prof.setSendData(true);
        }
        return hasInitByVision;
    }

    @Override
    public void initialize() {
        VisionMaster.Algorithm.POWER_CELLS.setAsCurrent();
        initLocationByVision();
    }

    @Override
    public void execute() {
        if(!hasInitByVision){
            if(!initLocationByVision()){
                return;
            }
        }
        if(JMP == System.currentTimeMillis() - t0){
            initLocationByVision();
        }
        prof.run();
    }

}
