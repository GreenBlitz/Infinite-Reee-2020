package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

public class FullShoot extends ParallelCommandGroup {
    private Supplier<Double> visionDist;

    public FullShoot(Supplier<Double> visionDist) {
        this.visionDist = visionDist;
    }

    @Override
    public void initialize() {
        double planeDistance = visionDist.get();
        double[] shooterState = RobotMap.Limbo2.Shooter.distanceToShooterState.linearlyInterpolate(planeDistance);
        addCommands(
                new TurretByVision(VisionMaster.Algorithm.HEXAGON),
                new DomeApproachSwiftly(shooterState[1]),
                new FullyAutoThreeStage(shooterState[0]),
                new SequentialCommandGroup(
//                        new WaitUntilCommand(() -> Shooter.getInstance().isPreparedToShoot());
                        new InsertIntoShooter(0.6, 0.6, 0.6)
                )
        );

    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        new StopShooter();
        new ResetDome();
    }
}
