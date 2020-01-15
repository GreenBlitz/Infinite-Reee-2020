package edu.greenblitz.bigRodika.commands.chassis.profiling;

import edu.greenblitz.gblib.threading.IThreadable;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.base.Vector2D;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ChassisProfiler2D;
import org.greenblitz.motion.profiling.MotionProfile2D;
import org.greenblitz.motion.profiling.ProfilingData;
import org.greenblitz.motion.profiling.followers.PidFollower2D;

import java.util.List;

public class Follow2DProfileCommand implements IThreadable {

    private MotionProfile2D profile2D;
    private PidFollower2D follower;
    private double linKv, linKa;
    private PIDObject perWheelPIDConsts;
    private PIDObject angularPIDConsts;
    private double collapsingPerWheelPIDTol;
    private double collapsingAngularPIDTol;

    private double maxPower;
    private boolean died;
    private boolean isOpp;

    /**
     *
     * @param path
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
    public Follow2DProfileCommand(List<State> path, double jump, int smoothingTail, ProfilingData data,
                                  double maxPower, double velMultLin, double accMultLin,
                                  PIDObject perWheelPIDCosnts, double collapseConstaPerWheel, PIDObject angularPIDConsts,
                                  double collapseConstAngular, boolean isReverse) {
        this.profile2D = ChassisProfiler2D.generateProfile(path, jump, data,
                0, 1.0, smoothingTail);
        this.linKv = velMultLin / data.getMaxLinearVelocity();
        this.linKa = accMultLin / data.getMaxLinearAccel();
        this.perWheelPIDConsts = perWheelPIDCosnts;
        this.collapsingPerWheelPIDTol = collapseConstaPerWheel;
        this.isOpp = isReverse;
        this.angularPIDConsts = angularPIDConsts;
        this.collapsingAngularPIDTol = collapseConstAngular;
        this.maxPower = maxPower;
    }

    private double mult;

    private long runTStart;
    private long minRuntime = 10;

    @Override
    public void atInit() {
        follower = new PidFollower2D(linKv, linKa, linKv, linKa,
                perWheelPIDConsts,
                collapsingPerWheelPIDTol, 1.0, angularPIDConsts, collapsingAngularPIDTol,
                RobotMap.BigRodika.Chassis.WHEEL_DIST,
                profile2D);
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

        if (isOpp){
            vals = vals.scale(-1);
        }

        if (!isOpp) {
            Chassis.getInstance().moveMotors(maxPower * clamp(vals.getX()),
                    maxPower * clamp(vals.getY()));
        } else  {
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

    public double clamp(double in){
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
        Chassis.getInstance().moveMotors(0,0);
    }

}