package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.Localizer;
import org.greenblitz.motion.base.Position;
import edu.greenblitz.bigRodika.utils.VisionMaster;

import java.util.function.Supplier;

public class ApproachSlow extends ChassisCommand { //TODO: make vision work
    private double targetD;
    private Supplier<Double> t_supplier;
    private double power;
    private double epsilon;

    public ApproachSlow(Supplier<Double> targetD) {
        this(targetD, 0.02, 0.03);
    }


    public ApproachSlow(Supplier<Double> targetD, double power, double epsilon) {
        this.t_supplier = targetD;
        this.power = power;
        this.epsilon = epsilon;
    }

    @Override
    public void initialize() {
        this.targetD = this.t_supplier.get();
    }

    @Override
    public void execute() {
        double currD = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();

        SmartDashboard.putNumber("Distance Error", Math.abs(currD - targetD));

        if (targetD < currD) {
            Chassis.getInstance().moveMotors(this.power, this.power);
        } else {
            Chassis.getInstance().moveMotors(-this.power, -this.power);
        }
    }

    @Override
    public void end(boolean interrupted) {
        Chassis.getInstance().moveMotors(0, 0);
        Chassis.getInstance().toBrake();
    }

    @Override
    public boolean isFinished() {
        double currD = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
        return Math.abs(currD - targetD) < this.epsilon;
    }
}