package edu.greenblitz.bigRodika.commands.chassis.profiling;

import edu.greenblitz.gblib.threading.IThreadable;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.base.Vector2D;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ChassisProfiler2D;
import org.greenblitz.motion.profiling.MotionProfile2D;
import org.greenblitz.motion.profiling.ProfilingData;
import org.greenblitz.motion.profiling.followers.PidFollower2D;
import org.greenblitz.motion.profiling.kinematics.CurvatureConverter;

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

    private double mult;

    private long runTStart;
    private long minRuntime = 5;


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
        SmartDashboard.putString("Data for profile", data.toString());
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
        follower = new PidFollower2D(linKv, linKa, linKv, linKa,
                perWheelPIDConsts,
                collapsingPerWheelPIDTol, Double.NaN, angularPIDConsts, collapsingAngularPIDTol,
                RobotMap.Limbo2.Chassis.WHEEL_DIST,
                profile2D);
        follower.setConverter(new CurvatureConverter(RobotMap.Limbo2.Chassis.WHEEL_DIST));
        follower.setSendData(true);
        Chassis.getInstance().toCoast();
        mult = isOpp ? -1 : 1;
        died = false;
        follower.init();
    }

    public void setSendData(boolean val){
        follower.setSendData(val);
    }

    @Override
    public void run() {
        runTStart = System.currentTimeMillis();

        Vector2D vals = follower.run(mult * Chassis.getInstance().getDerivedLeft(),
                mult * Chassis.getInstance().getDerivedRight(),
                mult * Chassis.getInstance().getAngularVelocityByWheels());

        if (isOpp){
            vals = vals.scale(-1);
        }

        if (Double.isNaN(vals.getX() + vals.getY())) {
            throw new RuntimeException("Profile returned NaN");
        }

        // --------------------
        // This code clamps both values of the motors between -maxPower and maxPower
        // while still keeping the same ratio


        // I think keeping the same ratio is bad, using old clamping.
        if (vals.getX() == 0 || vals.getY() == 0 || true) {

            vals.setX(maxPower*clamp(vals.getX()));
            vals.setY(maxPower*clamp(vals.getY()));

        } else {

            double ratio = vals.getY() / vals.getX();

            if (Math.abs(vals.getX()) > Math.abs(vals.getY())) {
                vals.setX(maxPower * clamp(vals.getX()));
                vals.setY(vals.getX() * ratio);
            } else {
                vals.setY(maxPower * clamp(vals.getY()));
                vals.setX(vals.getX() * (1.0 / ratio));
            }

        }

        // ---------------------

        SmartDashboard.putString("Prof vals", vals.toString());
        SmartDashboard.putBoolean("opp", isOpp);

        if (isOpp && false) { // Change later to !isOpp. prototype rob is dumb
            Chassis.getInstance().moveMotors(
                    vals.getX(),
                    vals.getY()
            );
//            Chassis.getInstance().moveMotors(0,
//                   0);
        } else  {
            Chassis.getInstance().moveMotors(vals.getY(),
                                            vals.getX());
        }

        if (minRuntime != 0) {
            try {
                Thread.sleep(minRuntime);
            } catch (InterruptedException e) {
                died = true;
                e.printStackTrace();
            }
        }
    }

    public double clamp(double in){
        return Math.copySign(
                Math.min(Math.abs(in), 1),
                                            in);
    }

    /**
     * @return if follower finished
     */
    @Override
    public boolean isFinished() {
        return follower.isFinished();// || died;
    }

    @Override
    public void atEnd() {
        System.out.println("Finished Course");
        Chassis.getInstance().toBrake();
        Chassis.getInstance().moveMotors(0,0);
    }

}
