package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.subsystems.Turret;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class TurretApproachSwiftlyRadians extends TurretApproachSwiftly {

    public TurretApproachSwiftlyRadians(double target, ITolerance tol) {
        super(target, tol);
    }

    public TurretApproachSwiftlyRadians(double target) {
        this(target, new AbsoluteTolerance(DEFAULT_TOLERANCE));
    }

    @Override
    public void execute() {
        turret.moveTurret(calculateVelocity(target - turret.getNormAngleRads()));
    }


}
