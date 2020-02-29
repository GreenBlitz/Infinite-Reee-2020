package edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage;

import com.revrobotics.CANPIDController;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.ShootBySimplePid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.pid.PIDObject;

public class StageThreePID extends ShootBySimplePid {


    public StageThreePID(PIDObject obj, double target) {
        super(obj, target);
    }

    @Override
    public void initialize() {
        CANPIDController controller = shooter.getPIDController();
        double ISum = controller.getIAccum();
        obj.setKf(obj.getKf() + ISum / target);
        obj.setKi(0);
        shooter.setPreparedToShoot(true);
        SmartDashboard.putNumber("I addition", ISum);
        super.initialize();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        shooter.setPreparedToShoot(false);
    }


}
