package edu.greenblitz.bigRodika.commands.turret.threaded;

import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.gblib.threading.IThreadable;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class TurretApproachSwiftlyThreaded implements IThreadable {

    protected double target;
    protected Turret turret;
    protected ITolerance tolerance;
    protected static final double DEFAULT_TOLERANCE = 0.003;//0.0015;

    public TurretApproachSwiftlyThreaded(double target, ITolerance tol) {
        super();
        this.target = target;
        this.tolerance = tol;
        this.turret = Turret.getInstance();
    }

    public TurretApproachSwiftlyThreaded(double target) {
        this(target, new AbsoluteTolerance(DEFAULT_TOLERANCE));
    }

    @Override
    public void run() {
        turret.moveTurret(
                TurretApproachSwiftly.calculateVelocity(target - turret.getTurretLocation()));
    }

    @Override
    public boolean isFinished() {
        return tolerance.onTarget(target, turret.getTurretLocation());
    }

    @Override
    public void atEnd() {
        turret.moveTurret(0);
    }

    @Override
    public void atInit() {

    }

}
