package edu.greenblitz.bigRodika.utils;

import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

public class BranchCommand extends GBCommand {

    private Command trueOp, falseOp;
    private Command chosen;
    private Supplier<Boolean> condition;

    public BranchCommand(Command ifTrue, Command ifFalse, Supplier<Boolean> cond) {
        trueOp = ifTrue;
        falseOp = ifFalse;

//        systems.addAll(ifFalse.getRequirements());
//        systems.addAll(ifTrue.getRequirements());

        condition = cond;
    }

    @Override
    public void initialize() {
        if (condition.get()) {
            chosen = trueOp;
        } else {
            chosen = falseOp;
        }

        chosen.initialize();
    }

    @Override
    public void execute() {
        chosen.execute();
    }

    @Override
    public void end(boolean interrupted) {
        chosen.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return chosen != null && chosen.isFinished();
    }

}
