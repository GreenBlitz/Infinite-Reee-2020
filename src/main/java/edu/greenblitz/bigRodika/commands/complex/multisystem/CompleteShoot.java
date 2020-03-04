package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.MotionUtils;
import edu.greenblitz.bigRodika.commands.turret.MoveTurretByConstant;
import edu.greenblitz.bigRodika.commands.turret.StopTurret;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.commands.turret.threaded.TurretByVisionThreaded;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionLocation;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
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
                new ThreadedCommand(new TurretByVisionThreaded(VisionMaster.Algorithm.HEXAGON),
                        Turret.getInstance()).withTimeout(2),
                new StopTurret(),
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
                            return
                                    VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
                        }
                        )
                )

        );
    }

}
