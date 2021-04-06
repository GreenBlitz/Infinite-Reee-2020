package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.Approach;
import edu.greenblitz.bigRodika.commands.chassis.approaches.ApproachAccurate;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.SemiAutomaticInsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.turret.StopTurret;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretToFront;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.TwoTuple;

import java.util.function.Supplier;

public class CompleteShootSkills extends ParallelCommandGroup {

    static Supplier<Double> distanceSupplier, visionDistanceSupplier, turretAlignSupp;

    public static final double TOLERANCE = 0.02;// 0.05 but 0.03 works no osilaitons

    public CompleteShootSkills() {


        visionDistanceSupplier = () -> VisionMaster.getInstance().getVisionLocation().getPlaneDistance();

        distanceSupplier = () -> {
            double visionDist = visionDistanceSupplier.get();
            TwoTuple<TwoTuple<Double, double[]>, TwoTuple<Double, double[]>> adjacents =
                    RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(visionDist);

            double dist1 = adjacents.getFirst().getFirst(), dist2 = adjacents.getSecond().getFirst();
            double delta1 = Math.abs(dist1 - visionDist),   delta2 = Math.abs(dist2 - visionDist);

            return (delta1 < delta2 ? dist1 : dist2) + 0.01;
        };

        PrepareShooterByDistance shoot = new PrepareShooterByDistance(visionDistanceSupplier){
            @Override
            public void initialize() {
                super.initialize();
                System.out.println("prepare shooter by dist");
            }
        };

        SequentialCommandGroup etc = new SequentialCommandGroup();

            ParallelCommandGroup stage1 = new ParallelCommandGroup();
            Supplier<Double> power = () -> Math.abs(RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_NEGATIVE_180 - Turret.getInstance().getRawTicks()) >
                Math.abs(RobotMap.Limbo2.Turret.ENCODER_VALUE_WHEN_FORWARD - Turret.getInstance().getRawTicks()) ? 0.05:-0.05;
                ApproachAccurate approach = new ApproachAccurate(distanceSupplier, power, 0.03);
                TurretByVision turret = new TurretByVision(VisionMaster.Algorithm.HEXAGON) {
                    @Override
                    public boolean isFinished() {
                        return approach.isFinished() && Math.abs(VisionMaster.getInstance().getVisionLocation().getRelativeAngleRad()) < TOLERANCE;
                    }

                    @Override
                    public void execute() {

                        if (!VisionMaster.getInstance().isLastDataValid() || Math.abs(VisionMaster.getInstance().getVisionLocation().getRelativeAngleRad()) < TOLERANCE) {
                            turret.moveTurret(0);
                            return;
                        }

                        turret.moveTurret(TurretApproachSwiftly.calculateVelocity(
                                Math.toRadians(VisionMaster.getInstance().getVisionLocation().getRelativeAngle())/(2*Math.PI)
                        ));
                    }

                };
            stage1.addCommands(approach, turret);

            StopTurret stage2 = new StopTurret();

            SemiAutomaticInsertIntoShooter stage3 = new SemiAutomaticInsertIntoShooter();

        etc.addCommands(stage1, stage2, stage3);


        this.addCommands(shoot, etc);
    }

    @Override
    public boolean isFinished() {
        return OI.getInstance().completeShootStop.get();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(true);

        new TurretToFront().schedule();
        new StopShooter().schedule();
        new ResetDome().schedule();
    }
}
