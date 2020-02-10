package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.commands.chassis.profiling.APPC;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.threading.IThreadable;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

public class GoFetch extends ChassisCommand {

    public static final double Y_OFFSET = -0.3;

    private ThreadedCommand cmd;

    public GoFetch() {
//        this.target = target.clone();
        // should be gotten by the network tables
    }

    @Override
    public void initialize() {
        VisionMaster.Algorithm.POWER_CELLS.setAsCurrent();

        ProfilingData data = RobotMap.Limbo2.Chassis.MotionData.POWER.get("0.5");

        double vN = data.getMaxLinearVelocity();
        double aN = data.getMaxLinearAccel();
        double vNr = data.getMaxAngularVelocity();
        double aNr = data.getMaxAngularAccel();

        IThreadable th = new APPC(

                () -> {
                    //VisionLocation location = VisionMaster.getInstance().getVisionLocation();

//                    double x = location.x;
//                    double y = location.y + Y_OFFSET;
//
//                    double ang;
//                    if (y == 0) {
//                        ang = 0;
//                    } else if (x == 0) {
//                        ang = Math.PI / 2;
//                    } else {
//                        ang = (Math.PI / 2) - Math.atan2(y * y - x * x, 2 * x * y);
//                    }
//
//                    return new State(location.x, location.y, ang);
                    return new State(0, 1, 0);
                },

                APPC.TargetMode.RELATIVE_TO_LOCALIZER, 0, data,
                0.5, 1.0, 1.0,
                new PIDObject(0*0.4/vN,0*0.001/vN,0*10.0/aN, 1),0.01*vN,
                new PIDObject(0*0.2/vNr,0,0*10.0/aNr, 1),0.01*vNr,
                false);

        cmd = new ThreadedCommand(th);
        cmd.initialize();
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
