package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.turret.TurretApproachSwiftlyRadians;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

import java.util.List;

public class PreShoot extends ParallelRaceGroup {

    public PreShoot(HexAlign align) {
        addCommands(
                new SequentialCommandGroup(
                        align,
//                        new ChassisTurretCompensate(),
                        new GBCommand() {
                            private ITolerance tol = new AbsoluteTolerance(Math.toRadians(1.0));

                            @Override
                            public void initialize() {
                                Chassis.getInstance().moveMotors(0, 0);
                                Chassis.getInstance().toBrake();
                            }

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
                        new ParallelRaceGroup(
                                new TurretApproachSwiftlyRadians(0, new AbsoluteTolerance(0.1)),
                                new WaitCommand(0.5)
                        ),
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

