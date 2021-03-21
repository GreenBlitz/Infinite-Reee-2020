package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;

public class ApprochSlow extends ChassisCommand{
    private double targetD;
    private double power;
    private double epsilon;

    public ApprochSlow(double targetD){
        this(targetD,0.1,0.1);
    }

    public ApprochSlow(double targetD, double power, double epsilon){
        this.targetD = targetD;
        this.power = power;
        this.epsilon = epsilon;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double currD = VisionMaster.getInstance().getVisionLocation().getFullDistance();
        if(targetD < currD){
            Chassis.getInstance().moveMotors(this.power,this.power);
        }else{
            Chassis.getInstance().moveMotors(-this.power,-this.power);
        }

    }

    @Override
    public void end(boolean interrupted) {
        Chassis.getInstance().moveMotors(0,0);
    }

    @Override
    public boolean isFinished() {
        double currD = VisionMaster.getInstance().getVisionLocation().getFullDistance();
        return Math.abs(currD - targetD) < this.epsilon;
    }
}