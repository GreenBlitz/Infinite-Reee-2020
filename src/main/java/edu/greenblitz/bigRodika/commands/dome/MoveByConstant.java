package edu.greenblitz.bigRodika.commands.dome;

public class MoveByConstant extends DomeCommand {

    private double power;

    public MoveByConstant(double p){
        super();
        power = p;
    }

    @Override
    public void execute() {
        dome.safeMove(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
