package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.commands.chassis.approaches.ApproachAccurate;
import edu.greenblitz.bigRodika.commands.chassis.approaches.ApproachRough;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

public class Approach extends SequentialCommandGroup {

    public Approach(Supplier<Double> distance) {
        super(new ApproachRough(distance), new ApproachAccurate(distance));
    }
}
