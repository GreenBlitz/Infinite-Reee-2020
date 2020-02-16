package edu.greenblitz.bigRodika.commands.chassis.profiling;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.IThreadable;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.base.Vector2D;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ChassisProfiler2D;
import org.greenblitz.motion.profiling.ProfilingData;
import org.greenblitz.motion.profiling.followers.PidFollower2D;

import java.util.ArrayList;
import java.util.List;

public class LiveProfileCommand implements IThreadable {

    private State endLocation;
    private PidFollower2D follower;
    private ProfilingData data;
    private double jump;
    private int smoothingTail;
    private double linKv, linKa;
    private PIDObject perWheelPIDConsts;
    private PIDObject angularPIDConsts;
    private double collapsingPerWheelPIDTol;
    private double collapsingAngularPIDTol;

    private double maxPower;
    private boolean died;
    private boolean isOpp;
    private double mult;
    private long runTStart;
    private long minRuntime = 10;
    /**
     * @param end
     * @param jump
     * @param smoothingTail
     * @param data
     * @param maxPower
     * @param velMultLin
     * @param accMultLin
     * @param perWheelPIDCosnts
     * @param collapseConstaPerWheel
     * @param angularPIDConsts
     * @param collapseConstAngular
     * @param isReverse
     */
    public LiveProfileCommand(State end, double jump, int smoothingTail, ProfilingData data,
                              double maxPower, double velMultLin, double accMultLin,
                              PIDObject perWheelPIDCosnts, double collapseConstaPerWheel, PIDObject angularPIDConsts,
                              double collapseConstAngular, boolean isReverse) {
        this.endLocation = end;
        this.jump = jump;
        this.smoothingTail = smoothingTail;
        this.data = data;
        this.linKv = velMultLin / data.getMaxLinearVelocity();
        this.linKa = accMultLin / data.getMaxLinearAccel();
        this.perWheelPIDConsts = perWheelPIDCosnts;
        this.collapsingPerWheelPIDTol = collapseConstaPerWheel;
        this.isOpp = isReverse;
        this.angularPIDConsts = angularPIDConsts;
        this.collapsingAngularPIDTol = collapseConstAngular;
        this.maxPower = maxPower;
    }

    @Override
    public void atInit() {

        List<State> path = new ArrayList<>(2);
        Position robLoc = Chassis.getInstance().getLocation();
        path.add(new State(robLoc.getX(), robLoc.getY(), robLoc.getAngle(), 0, 0));
        path.add(endLocation);

        follower = new PidFollower2D(linKv, linKa, linKv, linKa,
                perWheelPIDConsts,
                collapsingPerWheelPIDTol, 1.0, angularPIDConsts, collapsingAngularPIDTol,
                RobotMap.Limbo2.Chassis.WHEEL_DIST,
                ChassisProfiler2D.generateProfile(path, jump, data,
                        0, 1.0, smoothingTail));

        follower.setSendData(false);
        Chassis.getInstance().toCoast();
        mult = isOpp ? -1 : 1;
        died = false;
        follower.init();

    }


    @Override
    public void run() {
        runTStart = System.currentTimeMillis();

        Vector2D vals = follower.run(mult * Chassis.getInstance().getLeftRate(),
                mult * Chassis.getInstance().getRightRate(),
                mult * Chassis.getInstance().getAngularVelocityByGyro()); // TODO test if this is mult or -mult

        if (isOpp) {
            vals = vals.scale(-1);
        }

        if (!isOpp) {
            Chassis.getInstance().moveMotors(maxPower * clamp(vals.getX()),
                    maxPower * clamp(vals.getY()));
        } else {
            Chassis.getInstance().moveMotors(maxPower * clamp(vals.getY()),
                    maxPower * clamp(vals.getX()));
        }


        if (System.currentTimeMillis() - runTStart < minRuntime) {
            try {
                Thread.sleep(minRuntime - (System.currentTimeMillis() - runTStart));
            } catch (InterruptedException e) {
                e.printStackTrace();
                died = true;
            }
        }
    }

    public double clamp(double in) {
        return Math.copySign(Math.min(Math.abs(in), 1), in);
    }

    /**
     * @return if follower finished
     */
    @Override
    public boolean isFinished() {
        return follower.isFinished() || died;
    }

    @Override
    public void atEnd() {
        Chassis.getInstance().toBrake();
        Chassis.getInstance().moveMotors(0, 0);
    }

}
