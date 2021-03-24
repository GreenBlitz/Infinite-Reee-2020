package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import org.greenblitz.motion.Localizer;
import org.greenblitz.motion.base.Position;

public class ApproachSlow extends ChassisCommand { //TODO: make vision work
    private double targetD;
    private double power;
    private double epsilon;
    private Localizer localizer;


    public ApproachSlow(double targetD) {
        this(targetD, 0.1, 0.1);
    }

    public ApproachSlow(double targetD, double power, double epsilon) {
        this.targetD = targetD;
        this.power = power;
        this.epsilon = epsilon;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        //double currD = VisionMaster.getInstance().getVisionLocation().getFullDistance();
        Position currD = Localizer.getInstance().getLocation(); //we use the localizer for the testing
        if (targetD < currD.norm()) {
            Chassis.getInstance().moveMotors(this.power, this.power);
        } else {
            Chassis.getInstance().moveMotors(-this.power, -this.power);
        }
    }

    @Override
    public void end(boolean interrupted) {
        Chassis.getInstance().moveMotors(0, 0);
    }

    @Override
    public boolean isFinished() {
        double currD = Localizer.getInstance().getLocation().norm(); //VisionMaster.getInstance().getVisionLocation().getFullDistance();
        return Math.abs(currD - targetD) < this.epsilon;
    }
}