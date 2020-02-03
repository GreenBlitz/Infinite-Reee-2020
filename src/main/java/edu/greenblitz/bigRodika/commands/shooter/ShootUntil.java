package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.commands.funnel.inserter.ShootingMethod;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ShootUntil extends SequentialCommandGroup {


    public ShootUntil(@NotNull ShootingMethod pushMethod, @NotNull Supplier<Boolean> endCondition){

        if (pushMethod.isFinished()){
            throw new RuntimeException("Shooting method must never finish");
        }

        GBCommand endConditionCommand = new GBCommand() {
            @Override
            public boolean isFinished() {
                return endCondition.get();
            }
        };

        addCommands(new ParallelRaceGroup(pushMethod, endConditionCommand), new StopShooter());
    }

}
