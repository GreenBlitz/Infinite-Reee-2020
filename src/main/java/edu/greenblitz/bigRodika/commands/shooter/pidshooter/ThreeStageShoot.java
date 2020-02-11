package edu.greenblitz.bigRodika.commands.shooter.pidshooter;

import com.revrobotics.CANPIDController;
import edu.greenblitz.bigRodika.commands.shooter.ShootByConstant;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class ThreeStageShoot extends SequentialCommandGroup {

    public ThreeStageShoot(double target, double ff){


        addCommands(

                new ParallelRaceGroup(
                        new WaitUntilShooterSpeedClose(target, 100),
                        new ShootByConstant(1.0)
                ),

                new ParallelRaceGroup(
                        new ShootBySimplePid(
                                new PIDObject(0.0015,0.000004,0.0, ff), target
                        ),
                        new WaitUntilShooterSpeedClose(target, 8, 8) // Temp, replace by something better
                ),

                new StageThreePID(
                        new PIDObject(0.002,0.000004,0.00015, ff), target
                )
        );



    }

    public class StageThreePID extends ShootBySimplePid {

        public StageThreePID(PIDObject obj, double target) {
            super(obj, target);
        }

        @Override
        public void initialize() {
            SmartDashboard.putNumber("Stage3 start", shooter.getShooterSpeed());
            CANPIDController controller = shooter.getPIDController();
            double ISum = controller.getIAccum();
            obj.setKf(obj.getKf() + ISum * obj.getKi());
            obj.setKi(0);
            super.initialize();
        }

        @Override
        public void end(boolean interrupted) {
            super.end(interrupted);
            SmartDashboard.putNumber("Stage3 start", 0);
        }
    }

}
