package edu.greenblitz.bigRodika.commands.funnel.inserter;

import org.greenblitz.motion.pid.PIDObject;

public class InsertByPID extends InserterCommand {

    protected static final int PID_INDEX = 0;
    protected double target;

    public InsertByPID(PIDObject obj, double targetVelocity) {
        funnel.configurePID(PID_INDEX, obj);
        target = targetVelocity;
    }

    @Override
    public void initialize() {
        funnel.selectPIDLoop(PID_INDEX);
    }

    @Override
    public void execute() {
        funnel.moveInserterByPID(target);
    }

    @Override
    public void end(boolean interrupted) {
        funnel.moveInserter(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
