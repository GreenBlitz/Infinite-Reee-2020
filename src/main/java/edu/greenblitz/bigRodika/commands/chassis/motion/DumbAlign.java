package edu.greenblitz.bigRodika.commands.chassis.motion;

import org.greenblitz.motion.profiling.ProfilingConfiguration;

import java.util.List;

/**
 * This is like HexAlign, but only goes forwards.
 *
 * @author Alexey
 */
public class DumbAlign extends HexAlign {


    public DumbAlign(double r, double tolerance, double maxPower, ProfilingConfiguration config) {
        super(r, 1.0, 999.0, tolerance, maxPower, config);
    }

    public DumbAlign(List<Double> radsAndCritPoints, double tolerance, double maxPower, ProfilingConfiguration config) {
        super(radsAndCritPoints, 1.0, 999.0, tolerance, maxPower, config);
    }

    public DumbAlign(double r, double tolerance, double maxPower) {
        super(r, 1.0, 999.0, tolerance, maxPower);
    }


    public DumbAlign(List<Double> radsAndCritPoints, double tolerance, double maxPower) {
        super(radsAndCritPoints, 1.0, 999.0, tolerance, maxPower);
    }
}
