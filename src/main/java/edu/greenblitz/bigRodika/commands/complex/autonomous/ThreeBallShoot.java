package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.funnel.InsertIntoShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.ThreeStageShoot;
import edu.greenblitz.bigRodika.commands.turret.movebyp.TurretApproachSwiftly;
import edu.greenblitz.bigRodika.commands.turret.resets.ResetEncoderWhenInSide;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.State;

import java.util.ArrayList;
import java.util.List;

public class ThreeBallShoot extends SequentialCommandGroup{
    public ThreeBallShoot(){
        List<State> hardCodedShit = new ArrayList<>();
        hardCodedShit.add(new State(0, 0));
        hardCodedShit.add(new State(0, 1.0));
        addCommands(
//                new ResetEncoderWhenInSide(),
                new DomeApproachSwiftly(0.52),
                //(Command) new Follow2DProfileCommand(hardCodedShit, RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false)
                new ThreeStageShoot(1967, 0.1),
                new InsertIntoShooter(0.6, 0.6, 0.6),
                new ThreadedCommand(new Follow2DProfileCommand(hardCodedShit,
                        RobotMap.Limbo2.Chassis.MotionData.CONFIG, 0.3, false),
                        Chassis.getInstance())
        );
    }

    @Override
    public void initialize() {
        super.initialize();
        SmartDashboard.putBoolean("Auto init", true);
    }
}
