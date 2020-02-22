package edu.greenblitz.bigRodika.commands.chassis.turns;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class ChassisTurretCompensate extends GBCommand { // NOT A TURRET COMMAND!


    private static final ITolerance DEFAULT_TOLERANCE = new AbsoluteTolerance(4);
    private static final double DEFAULT_POWER = 0.3;

    private static final double MAX_ANGLE_RADS = Math.PI / 3;

    ITolerance tolerance;
    private double power;

    public ChassisTurretCompensate(ITolerance tol, double power) {
        tolerance = tol;
        this.power = power;
    }

    public ChassisTurretCompensate(ITolerance tol) {
        this(tol, DEFAULT_POWER);
    }

    public ChassisTurretCompensate(double power) {
        this(DEFAULT_TOLERANCE, power);
    }

    public ChassisTurretCompensate() {
        this(DEFAULT_TOLERANCE, DEFAULT_POWER);
    }

    @Override
    public void initialize() {
        System.out.println("Init ChassisTurretCompensate");
    }

    @Override
    public void execute() {
        if(Math.abs(Turret.getInstance().getNormAngleRads()) <= MAX_ANGLE_RADS){
            return;
        }
        double sign;
        if (VisionMaster.getInstance().isLastDataValid()) {
            sign = Math.signum(VisionMaster.getInstance().getVisionLocation().getRelativeAngle());
        } else {
            sign = -Math.signum(Turret.getInstance().getNormAngleRads());
        }
        Chassis.getInstance().moveMotors(-sign * power, sign * power);
    }

    @Override
    public boolean isFinished() {
        if (!VisionMaster.getInstance().isLastDataValid())
            return Math.abs(Turret.getInstance().getNormAngleRads()) <= MAX_ANGLE_RADS;
        return tolerance.onTarget(0, VisionMaster.getInstance().getVisionLocation().getRelativeAngle());
    }
}
