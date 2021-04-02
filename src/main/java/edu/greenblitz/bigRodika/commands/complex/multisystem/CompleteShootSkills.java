package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.ApproachSlow;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.funnel.SemiAutomaticInsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.ThreeStageShoot;
import edu.greenblitz.bigRodika.commands.turret.StopTurret;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.commands.turret.help.JustGoToTheFuckingTarget;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretToFront;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.TwoTuple;

import java.util.function.Supplier;

public class CompleteShootSkills extends ParallelCommandGroup {

    static Supplier<Double> distanceSupplier, visionDistanceSupplier, turretAlignSupp;

    public static final double TOLERANCE = 0.01;

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
                ApproachSlow approachSlow = new ApproachSlow(distanceSupplier){
                    @Override
                    public void initialize() {
                        super.initialize();
                    }
                };
                TurretByVision turret = new TurretByVision(VisionMaster.Algorithm.HEXAGON) {
                    @Override
                    public boolean isFinished() {
                        return approachSlow.isFinished() && Math.abs(VisionMaster.getInstance().getVisionLocation().getRelativeAngleRad()) < TOLERANCE;
                    }
                };
            stage1.addCommands(approachSlow, turret);

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
