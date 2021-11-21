package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.MotionUtils;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;

public class AimAndShoot extends GBCommand {
    boolean isFucked = false;

    public AimAndShoot() {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        VisionMaster.Algorithm.POWER_CELLS.setAsCurrent();
        VisionLocation location = VisionMaster.getInstance().getVisionLocation();
//        SmartDashboard.putString("Vision Location", location.toString());
        double[] difference = MotionUtils.getSimulatedVisionLocation();

        if (!VisionMaster.getInstance().isLastDataValid()) {
            isFucked = true;
            return;
        }

        double targetX = difference[0] + RobotMap.Limbo2.Chassis.VISION_CAM_X_DIST_CENTER;
        double targetY = difference[1] + RobotMap.Limbo2.Chassis.VISION_CAM_Y_DIST_CENTER;

    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
