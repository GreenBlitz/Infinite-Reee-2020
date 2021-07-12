package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SequentialFullShoot extends SequentialCommandGroup {
    public static double[][] data = {
            {1500, 0.25},
            {1600, 0.28},
            {1765, 0.32},
            {2100, 0.6},
            {2100, 0.36},
            {2350, 0.403},
            {2500, 0.48}
    };


    public SequentialFullShoot() {
        double domeVal = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
        double[] shooterState = linearlyInter(domeVal);
        addCommands(
                new TurretByVision(VisionMaster.Algorithm.HEXAGON).withTimeout(0.8),
                new DomeApproachSwiftly(shooterState[1]).withTimeout(0.10),
                new FullyAutoThreeStage(shooterState[0])
//                new ParallelCommandGroup(
//                        new InsertIntoShooter(0.5, 0.5, 0.5)
//                )
        );
    }

    public double[] linearlyInter(double domeVal){
        double[] res = new double[2];
        res[0] = (data[(int) Math.floor(domeVal)][0] + data[(int) Math.floor(domeVal) + 1][0]) / 2.0;
        res[1] = (data[(int) Math.floor(domeVal)][1] + data[(int) Math.floor(domeVal) + 1][1]) / 2.0;
        return res;
    }

}
