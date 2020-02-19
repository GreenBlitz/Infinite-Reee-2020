package edu.greenblitz.bigRodika.commands.dome;


public class ResetDome extends DomeMoveByConstant {

    public ResetDome(double power) {
        super(power);
    }

    @Override
    public void end(boolean interrupted) {
        if (!interrupted){
            dome.safeMove(0);
        }
    }

    @Override
    public boolean isFinished() {
        return dome.switchTriggered();
    }
}
