package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import org.greenblitz.motion.Localizer;
import org.greenblitz.motion.base.Position;
import edu.greenblitz.bigRodika.utils.VisionMaster;

import java.util.function.Supplier;

public class ApproachSlow extends ChassisCommand { //TODO: make vision work
    private double targetD;
    private double power;
    private double epsilon;


    public ApproachSlow(Supplier<Double> targetD) {
        this(targetD.get());
    }

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
        double currD = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
        if (targetD < currD) {
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
        double currD = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
        return Math.abs(currD - targetD) < this.epsilon;
    }
}