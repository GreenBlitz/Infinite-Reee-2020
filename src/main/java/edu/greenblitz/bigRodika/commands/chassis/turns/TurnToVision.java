package edu.greenblitz.bigRodika.commands.chassis.turns;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;

public class TurnToVision extends GBCommand {
    TurnToAngle turn;
    boolean fuck = false;

    public TurnToVision(){

    }

    @Override
    public void initialize(){
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();
        VisionMaster.getInstance().isLastDataValid();
        double[] diff = VisionMaster.getInstance().getVisionLocation().toDoubleArray();
        if(!VisionMaster.getInstance().isLastDataValid()) {
            fuck = true;
            return;
        }

        turn = new TurnToAngle(Math.toDegrees(Chassis.getInstance().getAngle() - Math.atan(diff[0]/diff[1])),3,1, RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularVelocity(),RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularAccel(),0.4);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interupted){
        if(fuck) return;
        turn.schedule();
    }
}
