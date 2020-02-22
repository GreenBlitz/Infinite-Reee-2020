package edu.greenblitz.bigRodika.commands.turret;

public class UnsafeResetTurret extends MoveTurretByConstant {
    /*
    WARN! this is very unsafe, consult @Peleg before use
    MIGHT DESTROY TURRET!!!
     */

    private static final double SAFETY_CONSTANT = 0.25;

    double startLoc;

    public UnsafeResetTurret(double power) {
        super(power);
    }

    @Override
    public void initialize() {
        startLoc = turret.getTurretLocation();
    }

    @Override
    public void end(boolean interrupted) {
        if (!interrupted) {
            turret.moveTurret(0);
        }
    }


    @Override
    public boolean isFinished() {
        return turret.isSwitchPressed() || Math.abs(turret.getTurretLocation() - startLoc) >= SAFETY_CONSTANT;
    }
}
