package edu.greenblitz.bigRodika.commands.complex.multisystem;


import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.FunnelCommand;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class FullShoot extends ParallelCommandGroup{
    public FullShoot(){
        double domeVal = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
        double[] shooterState = RobotMap.Limbo2.Shooter.distanceToShooterState.linearlyInterpolate(domeVal);
        addCommands(
                (Command) new TurretByVision(VisionMaster.Algorithm.HEXAGON),
                new DomeApproachSwiftly(shooterState[1]),
                new FullyAutoThreeStage(shooterState[0]),
                new InsertIntoShooter(0.5, 0.5, 0.5)
//                new SequentialCommandGroup(
//                        new WaitUntilCommand(() -> Shooter.getInstance().isPreparedToShoot())
//                        //TODO: add betterFunnel
//                )
        );
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        new StopShooter();
        new ResetDome();
    }
}
