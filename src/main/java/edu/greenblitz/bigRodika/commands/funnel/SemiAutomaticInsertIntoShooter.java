package edu.greenblitz.bigRodika.commands.funnel;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.AlternatingPush;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByConstant;
import edu.greenblitz.bigRodika.commands.intake.roller.RollByConstant;
import edu.greenblitz.bigRodika.subsystems.Funnel;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class SemiAutomaticInsertIntoShooter extends SequentialCommandGroup {


    public SemiAutomaticInsertIntoShooter(){
        this(RobotMap.Limbo2.Funnel.SemiAuto.pulseInTime, RobotMap.Limbo2.Funnel.SemiAuto.pulseOutTime, RobotMap.Limbo2.Funnel.SemiAuto.pusherInPower,
                RobotMap.Limbo2.Funnel.SemiAuto.pusherOutPower, RobotMap.Limbo2.Funnel.SemiAuto.insertByConstant, RobotMap.Limbo2.Funnel.SemiAuto.rollByConstant);
    }
    public SemiAutomaticInsertIntoShooter(double pulseInTime, double pulseOutTime, double pusherInPower, double pusherOutPower,
    double insertByConstant, double rollByConstant) {
        addCommands(
                new WaitUntilCommand(() -> Shooter.getInstance().isPreparedToShoot()),
                new ParallelCommandGroup(
                        new AlternatingPush(pusherInPower,pusherOutPower,pulseInTime,pulseOutTime,0),
                        new InsertByConstant(insertByConstant),
                        new RollByConstant(rollByConstant)
                )
        );
    }
}
