package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

import java.util.Set;

public class FullShoot extends SequentialCommandGroup {

    private double target;
    private final double MAX_ERROR = 0.15;
    private final long STABLE_TIME = 100;

    public FullShoot(double targetSpeed){
        this.target = targetSpeed;

        PIDObject stage2Pid = new PIDObject(0,0,0);
        PIDObject stage3Pid = new PIDObject(0,0,0);

        addCommands(new ParallelRaceGroup(new ShootByConstant(1), new WaitUntilShooterSpeedClose(target)),
                new SetUpTargetSpeed(stage2Pid, target,MAX_ERROR, STABLE_TIME),
                new StayOnTargetSpeed(stage3Pid, target));

    }
}
