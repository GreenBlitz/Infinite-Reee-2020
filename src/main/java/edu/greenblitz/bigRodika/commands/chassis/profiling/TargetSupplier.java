package edu.greenblitz.bigRodika.commands.chassis.profiling;

import org.greenblitz.motion.base.State;

@FunctionalInterface
public interface TargetSupplier {

    State getTarget();

}
