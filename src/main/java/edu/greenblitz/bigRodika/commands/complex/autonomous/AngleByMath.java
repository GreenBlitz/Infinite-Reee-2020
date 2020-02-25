package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftlyRadians;
import org.greenblitz.motion.Localizer;
import org.greenblitz.motion.tolerance.ITolerance;

public class AngleByMath extends TurretApproachSwiftlyRadians {

    public static final double DIST = Math.sqrt(Math.pow(3.0 + 3.9 - 1.0, 2) +
            Math.pow(2.154 - 0.704, 2));
    public static final double ANG = Math.toRadians(11.5);

    public AngleByMath(double target, ITolerance tol) {
        super(target, tol);
    }

    @Override
    public void initialize() {
        try {
            double d = Math.abs(Localizer.getInstance().getLocation().getX());
            target = Math.atan(DIST * Math.sin(ANG) / (DIST * Math.cos(Math.PI - ANG) + d));
            target *= -1;
        } catch (Exception e){
            target = -ANG;
        }
        super.initialize();
    }
}
