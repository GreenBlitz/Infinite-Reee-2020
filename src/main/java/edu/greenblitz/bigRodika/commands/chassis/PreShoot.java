package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.turns.TurnToVision;
import edu.greenblitz.bigRodika.commands.generic.WaitMiliSeconds;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import java.util.ArrayList;
import java.util.List;

public class PreShoot extends SequentialCommandGroup {

    public PreShoot() {
        List<Double> rds = new ArrayList<>();
        rds.add(6.0);
        HexAlign hexAlign = new HexAlign(6.0, 0.2);
        //HexAlign hexAlign = new HexAlign();
        addCommands(hexAlign,

                new WaitMiliSeconds(300),

                new TurnToVision(VisionMaster.Algorithm.HEXAGON,
                        RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5").getMaxAngularVelocity(),
                        RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5").getMaxAngularAccel(),
                        0.5, hexAlign));
    }
}

