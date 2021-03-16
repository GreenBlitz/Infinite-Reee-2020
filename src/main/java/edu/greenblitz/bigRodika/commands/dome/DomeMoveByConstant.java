package edu.greenblitz.bigRodika.commands.dome;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    public void end(boolean interrupted) {
        dome.safeMove(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
