package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.bigRodika.commands.chassis.turns.TurnToVision;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PreShoot extends SequentialCommandGroup {

    public PreShoot(){
        HexAlign hexAlign = new HexAlign();
        addCommands(hexAlign,new TurnToVision(VisionMaster.Algorithm.HEXAGON,RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularVelocity(),RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularAccel(),0.4));
    }
}
