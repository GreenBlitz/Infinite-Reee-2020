package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.turns.ChassisTurretCompensate;
import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftlyRadians;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

import java.util.List;

public class PreShoot extends ParallelRaceGroup {

    public PreShoot(HexAlign align) {
        addCommands(
                new SequentialCommandGroup(
                        align,
                        new ChassisTurretCompensate(),
                        new GBCommand() {
                            private ITolerance tol = new AbsoluteTolerance(1.0);

                            @Override
                            public boolean isFinished() {
                                double[] diff = VisionMaster.getInstance().getVisionLocation().toDoubleArray();

                                if (!VisionMaster.getInstance().isLastDataValid()) {
                                    return false;
                                }

                                return tol.onTarget(
                                        RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET,
                                        Math.atan(diff[0] / diff[1]));
                            }
                        }
                ),
                new SequentialCommandGroup(
                        new TurretApproachSwiftlyRadians(0),
                        new TurretByVision(VisionMaster.Algorithm.HEXAGON)
                )
        );
    }

    public PreShoot(double radius) {
        this(new HexAlign(radius, 0.2, 0.5, 0.1, 0.5));

    }

    public PreShoot(List<Double> radsAndCritPoints) {
        this(new HexAlign(radsAndCritPoints, 0.2, 0.5, 0.1, 0.5));
    }

}

