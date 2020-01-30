package edu.greenblitz.bigRodika.commands.shooter;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class FullShoot extends SequentialCommandGroup {
    private final static double MAX_ERROR_STAGE_1 = 0.15;
    private final static double MAX_ERROR_STAGE_2 = 0.15;
    private final static long STABLE_TIME = 100;

    public FullShoot(double targetSpeed) {

        PIDObject stage2Pid = new PIDObject(0.01, 0, 0);
        PIDObject stage3Pid = new PIDObject(0.01, 0, 0);
        addCommands(new ParallelRaceGroup(new ShootByConstant(1), new WaitUntilShooterSpeedClose(targetSpeed,MAX_ERROR_STAGE_1)),
                new SetUpTargetSpeed(stage2Pid, targetSpeed, MAX_ERROR_STAGE_2, STABLE_TIME),
                new ShootBySimplePid(stage3Pid, targetSpeed));

    }
}
