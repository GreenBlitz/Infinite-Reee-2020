package edu.greenblitz.bigRodika.commands.turret.threaded;

import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftly;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class TurretApproachSwiftlyRadiansThreaded extends TurretApproachSwiftlyThreaded {

    public TurretApproachSwiftlyRadiansThreaded(double target, ITolerance tol) {
        super(target, tol);
    }

    public TurretApproachSwiftlyRadiansThreaded(double target) {
        this(target, new AbsoluteTolerance(DEFAULT_TOLERANCE));
    }

    @Override
    public void run() {
        turret.moveTurret(TurretApproachSwiftly.calculateVelocity(target - turret.getNormAngleRads()));
    }


}
