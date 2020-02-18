package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.turns.TurnToVision;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.bigRodika.utils.WaitMiliSeconds;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PreShoot extends SequentialCommandGroup {

    public PreShoot() {
        this(6.0);
    }

    public PreShoot(double radius){
        this(radius, false);
    }

    public PreShoot(double radius, boolean autoInnerHole) {
        HexAlign hexAlign = new HexAlign(radius, 0.2, 0.5, 0.1, 0.5, autoInnerHole, 5, 10);
        addCommands(hexAlign,

                new WaitMiliSeconds(500),

                new TurnToVision(VisionMaster.Algorithm.HEXAGON,
                        RobotMap.Limbo2.Chassis.MotionData.POWER.get("0.5").getMaxAngularVelocity(),
                        RobotMap.Limbo2.Chassis.MotionData.POWER.get("0.5").getMaxAngularAccel(),
                        0.5, autoInnerHole, hexAlign));
    }
}

