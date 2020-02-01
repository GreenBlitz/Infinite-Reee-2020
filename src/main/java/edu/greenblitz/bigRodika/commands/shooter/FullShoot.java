package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.commands.shooter.pidshooter.WaitUntilShooterSpeedClose;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class FullShoot extends SequentialCommandGroup {
    private final static double MAX_ERROR_STAGE_1 = 100;
    private final static double MAX_ERROR_STAGE_2 = 10;
    private final static long STABLE_TIME = 1000;

    public FullShoot(double targetSpeed) {

//        PIDObject stage2Pid = new PIDObject(1e-3, 1e-6, 0);
//        PIDObject stage3Pid = new PIDObject(1e-3, 1e-6, 0);
        PIDObject stage3Pid = new PIDObject(1e-4, 0, 0);
        addCommands(new ParallelRaceGroup(new ShootByConstant(1), new WaitUntilShooterSpeedClose(targetSpeed, MAX_ERROR_STAGE_1)),
//                new SetUpTargetSpeedForHadasa(stage2Pid, targetSpeed, MAX_ERROR_STAGE_2, STABLE_TIME),
                new ShootBySimplePidForHadasa(stage3Pid, targetSpeed));
    }

}
