package edu.greenblitz.bigRodika.commands.turret.help;

import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class TurretToDefaultGood extends JustGoToTheFuckingTarget {

    private JoystickButton[] blockers;

    public TurretToDefaultGood(
            JoystickButton[] blockers, double tolerance,
            double slowDownBegin, double slowDownEnd, double maximumSpeed, double minimumSpeed, double speedUpSlope) {
        super(() -> -Math.PI + Math.toRadians(30.0), tolerance, slowDownBegin, slowDownEnd, maximumSpeed, minimumSpeed, speedUpSlope);
        this.blockers = blockers;
    }

    @Override
    public void execute() {
        if (DriverStation.getInstance().isAutonomous()){
            return;
        }
        if (Turret.getInstance().getCurrentCommand() == Turret.getInstance().getCurrentCommand()) {
            for (JoystickButton butt : blockers){
                if (butt.get()) return;
            }
            super.execute();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
