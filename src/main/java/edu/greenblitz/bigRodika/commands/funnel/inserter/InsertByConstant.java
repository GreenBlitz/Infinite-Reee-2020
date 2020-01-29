package edu.greenblitz.bigRodika.commands.funnel.inserter;

public class InsertByConstant extends InserterCommand {

    private double power;

    public InsertByConstant(double power){
        super();
        this.power = power;
    }

    @Override
    public void execute() {
        funnel.moveInserter(power);
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
