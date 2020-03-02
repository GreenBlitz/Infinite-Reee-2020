package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.MotionUtils;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.interpolation.Dataset;

public class CompleteShoot extends SequentialCommandGroup {

    public CompleteShoot() {
        addCommands(

                new MoveTurretByConstant(0.4).withInterrupt(() ->
                        VisionMaster.getInstance().isLastDataValid() &&
                                Math.abs(VisionMaster.getInstance().getVisionLocation().getRelativeAngle()) < 10)
                        .withTimeout(2.5),
                new GBCommand() {
                    @Override
                    public boolean isFinished() {
                        if (!VisionMaster.getInstance().isLastDataValid()) return false;
                        double planaryDist = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();

                        return planaryDist < RobotMap.Limbo2.Shooter.MAXIMUM_SHOOT_DIST
                                && planaryDist > RobotMap.Limbo2.Shooter.MINIMUM_SHOOT_DIST;
                    }
                },
                new ParallelCommandGroup(
                        new PrepareShooterByDistance(() -> {
                            double[] simLoc = MotionUtils.getSimulatedVisionLocation();
                            return Math.sqrt(Math.pow(simLoc[0], 2) + Math.pow(simLoc[1], 2));
                        }
                        ),
                        new TurretByVision(VisionMaster.Algorithm.HEXAGON).withInterrupt(() ->
                                Math.abs(VisionMaster.getInstance().getVisionLocation().getRelativeAngle()) < 1.0
                        ).withTimeout(2)
                )

        );
    }

}
