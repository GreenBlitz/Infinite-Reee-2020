package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class FullShoot extends ParallelCommandGroup{
    public FullShoot(){
        double planeDistance =  VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
        double[] shooterState;
        try {
            shooterState = RobotMap.Limbo2.Shooter.distanceToShooterState.linearlyInterpolate(planeDistance);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            return;
        }
        addCommands(
                (Command) new TurretByVision(VisionMaster.Algorithm.HEXAGON), //FIXME: Use this when raz can C#
                //new TurretApproachSwiftly(0.2), // FIXME: delete this when  raz can C#
                //new DomeApproachSwiftly(shooterState[1]),
                new FullyAutoThreeStage(shooterState[0]),
                new SequentialCommandGroup(
                        new WaitUntilCommand(() -> Shooter.getInstance().isPreparedToShoot())
                        //TODO: add betterFunnel
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
