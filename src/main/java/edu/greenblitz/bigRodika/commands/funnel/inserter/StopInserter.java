package edu.greenblitz.bigRodika.commands.funnel.inserter;

public class StopInserter extends ShootingMethod {

    @Override
    public void initialize() {
        funnel.moveInserter(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
