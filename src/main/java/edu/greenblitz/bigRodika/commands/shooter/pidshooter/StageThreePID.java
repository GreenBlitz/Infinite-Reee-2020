package edu.greenblitz.bigRodika.commands.shooter.pidshooter;

import com.revrobotics.CANPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.pid.PIDObject;

public class StageThreePID extends ShootBySimplePid {


    public StageThreePID(PIDObject obj, double target) {
        super(obj, target);
    }

    @Override
    public void initialize() {
        SmartDashboard.putNumber("Stage3 start", shooter.getShooterSpeed());
        CANPIDController controller = shooter.getPIDController();
        double ISum = controller.getIAccum();
        obj.setKf(obj.getKf() + ISum/target);
        SmartDashboard.putString("I addtion", Double.toString(ISum/target));
        obj.setKi(0);
        super.initialize();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        SmartDashboard.putNumber("Stage3 start", 0);
    }


}
