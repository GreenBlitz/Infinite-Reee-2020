package edu.greenblitz.bigRodika.commands.chassis.profiling;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.IThreadable;
import org.greenblitz.motion.Localizer;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.Position;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.base.Vector2D;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ChassisProfiler2D;
import org.greenblitz.motion.profiling.MotionProfile2D;
import org.greenblitz.motion.profiling.ProfilingData;
import org.greenblitz.motion.profiling.followers.PidFollower2D;
import org.greenblitz.motion.profiling.kinematics.CurvatureConverter;

import java.awt.event.PaintEvent;
import java.util.ArrayList;
import java.util.List;


public class APPC implements IThreadable {

    private static final double JUMP = 0.001;
    private static final int TAIL = 1000;

    public enum TargetMode {
        RELETIVE_TO_ROBOT,
        RELATIVE_TO_LOCALIZER
    }

    private MotionProfile2D profile2D;
    private ProfilingData data;
    private PidFollower2D follower;
    private TargetSupplier supplier;
    private double linKv, linKa;
    private PIDObject perWheelPIDConsts;
    private PIDObject angularPIDConsts;
    private double vEnd;
    private double collapsingPerWheelPIDTol;
    private double collapsingAngularPIDTol;
    private double finalProfileThreshold = 0.4;
    private long lastCalculationTime = 0;
    private long followTime = 150;
    private long delay = 0;
    private boolean finalStage;

    private List<State> path;
    private TargetMode mode;

    private double maxPower;
    private boolean isOpp;


    private double mult;


    public APPC(TargetSupplier supplier,
                TargetMode mode,
                double vEnd, ProfilingData data,
                double maxPower,
                double linKv, double linKa,
                PIDObject perWheelPIDCosnts, double collapseConstaPerWheel,
                PIDObject angularPIDConsts, double collapseConstAngular,
                boolean isReverse) {
        this.linKv = linKv / data.getMaxLinearVelocity();
        this.linKa = linKa / data.getMaxLinearAccel();
        this.mode = mode;
        this.perWheelPIDConsts = perWheelPIDCosnts;
        this.collapsingPerWheelPIDTol = collapseConstaPerWheel;
        this.isOpp = isReverse;
        this.angularPIDConsts = angularPIDConsts;
        this.collapsingAngularPIDTol = collapseConstAngular;
        this.maxPower = maxPower;
        this.supplier = supplier;
        this.vEnd = vEnd;
        this.data = data;
        this.path = new ArrayList<>(2);
        path.add(new State(0, 0, 0));
        path.add(new State(0, 0, 0));
    }

    @Override
    public void atInit() {
        follower = new PidFollower2D(linKv, linKa, linKv, linKa,
                perWheelPIDConsts,
                collapsingPerWheelPIDTol, Double.NaN, angularPIDConsts, collapsingAngularPIDTol,
                RobotMap.Limbo2.Chassis.WHEEL_DIST,
                null);
        follower.setConverter(new CurvatureConverter(RobotMap.Limbo2.Chassis.WHEEL_DIST));
        follower.setSendData(true);
        Chassis.getInstance().toCoast();
        mult = isOpp ? -1 : 1;
        follower.init();
        finalStage = false;
    }

    public void setSendData(boolean val) {
        follower.setSendData(val);
    }

    @Override
    public void run() {

        Vector2D vals;

        if (finalStage) {

            if (delay != 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            vals = follower.run(mult * Chassis.getInstance().getDerivedLeft(),
                    mult * Chassis.getInstance().getDerivedRight(),
                    mult * Chassis.getInstance().getAngularVelocityByWheels());

        } else {

            if(lastCalculationTime == 0) Chassis.getInstance().putNumber("trash", Point.subtract(path.get(1), path.get(0)).norm());

            boolean weDone = false;

            if (System.currentTimeMillis() - lastCalculationTime > followTime) {

                path.set(1, supplier.getTarget());

                switch (mode) {
                    case RELATIVE_TO_LOCALIZER:
                        Position loc = Chassis.getInstance().getLocation();
                        path.set(0, new State(loc.getX(), loc.getY(), -loc.getAngle()));
                        break;
                    case RELETIVE_TO_ROBOT:
                        path.set(0, new State(0, 0, 0));
                        break;
                }

                path.get(0).setLinearVelocity(Chassis.getInstance().getLinearVelocity());
                path.get(0).setAngularVelocity(Chassis.getInstance().getAngularVelocityByWheels());

                weDone = Point.subtract(path.get(1), path.get(0)).norm() <= finalProfileThreshold;

                // If we are finishing, small angle error is better than robot doing dumb shit
                if (weDone) {
                    path.get(1).setAngle(
                            getEasiestAngle(Point.subtract(path.get(1), path.get(0)
                                    .rotate(-path.get(0).getAngle())))
                    );
                }

                this.profile2D = ChassisProfiler2D.generateProfile(path,
                        JUMP,
                        Chassis.getInstance().getLinearVelocity(), vEnd,
                        data, 0,
                        0.7,
                        TAIL);

                follower.setProfile(profile2D);

                lastCalculationTime = System.currentTimeMillis();

            } else {
                if (delay != 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            vals = follower.forceRun(mult * Chassis.getInstance().getDerivedLeft(),
                    mult * Chassis.getInstance().getDerivedRight(),
                    mult * Chassis.getInstance().getAngularVelocityByWheels(),
                    (System.currentTimeMillis() - lastCalculationTime)/1000.0 + 0.01);

            if (weDone) {
                finalStage = true;
                follower.init();
            }
        }

        vals = ProfilingUtils.Clamp(ProfilingUtils.flipToBackwards(vals, isOpp), maxPower);

        Chassis.getInstance().moveMotors(vals.getX(), vals.getY());

    }

    /**
     * @return if follower finished
     */
    @Override
    public boolean isFinished() {
        return finalStage && follower.isFinished();
    }

    @Override
    public void atEnd() {
        Chassis.getInstance().toBrake();
        Chassis.getInstance().moveMotors(0, 0);
    }

    protected double getEasiestAngle(Point pos) {
        double x = pos.getX();
        double y = pos.getY();

        double ang;
        if (x == 0) {
            ang = 0;
        } else if (y == 0) {
            ang = Math.PI / 2;
        } else {
            ang = (Math.PI / 2) - Math.atan2(y * y - x * x, 2 * x * y);
        }

        return ang;
    }

}