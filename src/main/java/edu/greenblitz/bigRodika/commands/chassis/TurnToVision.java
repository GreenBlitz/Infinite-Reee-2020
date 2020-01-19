package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;

public class TurnToVision extends GBCommand {
    TurnToAngle turn;

    public TurnToVision(){

    }

    @Override
    public void initialize(){
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        double[] diff = VisionMaster.getInstance().getVisionLocation().toDoubleArray();
        turn = new TurnToAngle(Math.toDegrees(Chassis.getInstance().getAngle() - Math.atan(diff[0]/diff[2])),10,2, RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.7").getMaxAngularVelocity(),RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.7").getMaxAngularVelocity(),0.7);
    }
}
