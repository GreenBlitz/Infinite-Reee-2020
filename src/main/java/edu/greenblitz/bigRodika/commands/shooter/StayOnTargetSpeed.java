package edu.greenblitz.bigRodika.commands.shooter;

import org.greenblitz.motion.pid.PIDObject;

public class StayOnTargetSpeed extends ShootBySimplePid{

    private double target;

    public StayOnTargetSpeed(PIDObject obj, double target){
        super(obj,target);
    }


}
