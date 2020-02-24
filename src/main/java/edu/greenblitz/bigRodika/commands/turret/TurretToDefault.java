package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Shifter;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.bigRodika.subsystems.Turret;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;

public class TurretToDefault extends TurretApproachSwiftly {


    public TurretToDefault() {
        super(
                RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_NEGATIVE_180 / RobotMap.Limbo2.Turret.NORMALIZER.getValue()
        , new AbsoluteTolerance(0.0025));
    }

    @Override
    public void execute() {
        if (Shooter.getInstance().getCurrentCommand() == Shooter.getInstance().getDefaultCommand()) {
            if (tolerance.onTarget(target, turret.getTurretLocation())){
                turret.moveTurret(0);
            } else {
                super.execute();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

