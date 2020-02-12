package edu.greenblitz.bigRodika.commands.complex;

import edu.greenblitz.bigRodika.commands.intake.extender.ExtendRoller;
import edu.greenblitz.bigRodika.commands.intake.extender.RetractRoller;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SimpleIntake extends SequentialCommandGroup {
    public SimpleIntake(double power) {
        addCommands(new ParallelCommandGroup(new RollByConstant(power), new ExtendRoller()),
                new RetractRoller());
    }
}
