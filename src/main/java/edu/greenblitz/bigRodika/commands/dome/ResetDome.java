package edu.greenblitz.bigRodika.commands.dome;


public class ResetDome extends DomeMoveByConstant {

    private int count;

    public ResetDome(double power) {
        super(power);
    }

    @Override
    public void initialize() {
        super.initialize();
        count = 0;
    }

    @Override
    public void end(boolean interrupted) {
        if (!interrupted) {
            dome.safeMove(0);
        }
    }

    @Override
    public void execute() {
        super.execute();
        if (dome.switchTriggered()){
            count += 1;
        }
    }

    @Override
    public boolean isFinished() {
        return count > 5;
    }
}
