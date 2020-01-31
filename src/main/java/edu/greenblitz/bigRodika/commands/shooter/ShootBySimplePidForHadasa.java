package edu.greenblitz.bigRodika.commands.shooter;

import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.pid.PIDObject;

public class ShootBySimplePidForHadasa extends GBCommand {
    private CANPIDController m_pidController;
    private double setPoint;

    public ShootBySimplePidForHadasa(double kP, double kI, double kD, double setPoint) {
        this.m_pidController = Shooter.getInstance().getPIDController();
        this.setPoint = setPoint;

        double kIz = 0,
                kFF = 0.000156,
                kMaxOutput = 1,
                kMinOutput = -1,
                maxVel = 5000,
                minVel = 0,
                maxAcc = 1500,
                allowedErr = 0;

        m_pidController.setP(kP);
        m_pidController.setI(kI);
        m_pidController.setD(kD);
        m_pidController.setIZone(kIz);
        m_pidController.setFF(kFF);
        m_pidController.setOutputRange(kMinOutput, kMaxOutput);

        int smartMotionSlot = 0;
        m_pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        m_pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        m_pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        m_pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
    }

    public ShootBySimplePidForHadasa(PIDObject obj, double setPoint) {
        this(obj.getKp(), obj.getKi(), obj.getKd(), setPoint);
    }

    public void execute() {
        m_pidController.setReference(setPoint, ControlType.kVelocity);
    }

    @java.lang.Override
    public boolean isFinished() {
        return false;
    }
}
