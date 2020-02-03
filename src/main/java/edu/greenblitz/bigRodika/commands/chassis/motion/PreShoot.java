package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.turns.TurnToVision;
import edu.greenblitz.bigRodika.commands.generic.WaitMiliSeconds;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PreShoot extends SequentialCommandGroup {

    public PreShoot() {
        HexAlign hexAlign = new HexAlign(4.0, 0.2);
        //HexAlign hexAlign = new HexAlign();
        addCommands(hexAlign,

                new WaitMiliSeconds(1000),

                new TurnToVision(VisionMaster.Algorithm.HEXAGON,
                        RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5").getMaxAngularVelocity(),
                        RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.5").getMaxAngularAccel(),
                        0.5, hexAlign));
    }
}

