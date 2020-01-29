package edu.greenblitz.bigRodika.commands.funnel;

public class InsertByConstant extends FunnelCommand {

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
}
