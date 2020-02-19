package edu.greenblitz.bigRodika.commands.dome;

public class DomeMoveByConstant extends DomeCommand {

    private double power;

    public DomeMoveByConstant(double p) {
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
