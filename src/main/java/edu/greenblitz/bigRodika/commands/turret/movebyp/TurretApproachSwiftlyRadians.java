package edu.greenblitz.bigRodika.commands.turret.movebyp;

<<<<<<< HEAD:src/main/java/edu/greenblitz/bigRodika/commands/turret/movebyp/TurretApproachSwiftlyRadians.java
=======
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
>>>>>>> district-1:src/main/java/edu/greenblitz/bigRodika/commands/turret/TurretApproachSwiftlyRadians.java
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class TurretApproachSwiftlyRadians extends TurretApproachSwiftly {

    public TurretApproachSwiftlyRadians(double target, ITolerance tol) {
        super(target, tol);
    }

    public TurretApproachSwiftlyRadians(double target) {
        this(target, new AbsoluteTolerance(DEFAULT_TOLERANCE));
    }

    @Override
    public void execute() {
        turret.moveTurret(calculateVelocity(target - turret.getNormAngleRads()));
    }


}
