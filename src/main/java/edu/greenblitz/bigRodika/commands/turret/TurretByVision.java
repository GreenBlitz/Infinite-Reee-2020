package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretByVision extends TurretCommand {

    private VisionMaster.Algorithm algorithm;

    public TurretByVision(VisionMaster.Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public static final double SLOW_DOWN_BEGIN = TurretApproachSwiftly.SLOW_DOWN_BEGIN;
    public static final double SLOW_DOWN_END = TurretApproachSwiftly.SLOW_DOWN_END;
    public static final double MINIMUM_SPEED = TurretApproachSwiftly.MINIMUM_SPEED;
    public static final double MAXIMUM_SPEED = TurretApproachSwiftly.MAXIMUM_SPEED;

    public static final double SLOPE = (MAXIMUM_SPEED - MINIMUM_SPEED) / (SLOW_DOWN_BEGIN - SLOW_DOWN_END);
    public static final double X_OFFSET = SLOW_DOWN_END;
    public static final double Y_OFFSET = MINIMUM_SPEED;

    @Override
    public void initialize() {
        algorithm.setAsCurrent();
        System.out.println("Start TurretByVision");
    }

    @Override
    public void execute() {

        if (!VisionMaster.getInstance().isLastDataValid()) {
            turret.moveTurret(0);
            return;
        }

        turret.moveTurret(TurretApproachSwiftly.calculateVelocity(
                Math.toRadians(VisionMaster.getInstance().getVisionLocation().getRelativeAngle())/(2*Math.PI)
                ));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        turret.moveTurret(0);
    }

}
