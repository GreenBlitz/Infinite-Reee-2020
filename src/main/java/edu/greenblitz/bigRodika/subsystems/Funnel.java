package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.BaseTalonPIDSetConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.PIDFeed;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.greenblitz.gblib.gears.GearDependentValue;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Funnel implements Subsystem {
    private static Funnel instance;

    private TalonSRX m_funnel, m_feed;
    private TalonEncoder m_encoder;
    private BaseTalonPIDSetConfiguration m_pid;

    private Funnel() {
        this.m_funnel = new TalonSRX(RobotMap.BigRodika.Funnel.funnelPort);
        this.m_feed = new TalonSRX(RobotMap.BigRodika.Funnel.feedPort);
        GearDependentValue<Double> norm = new GearDependentValue<Double>(RobotMap.BigRodika.Funnel.Encoder.whenPower, RobotMap.BigRodika.Funnel.Encoder.whenSpeed);
        this.m_encoder = new TalonEncoder(norm, this.m_feed);
        //missing pid
    }

    public static Funnel getInstance() {
        if(instance == null) {
            instance = new Funnel();
            CommandScheduler.getInstance().registerSubsystem(instance);
            instance.setDefaultCommand(
                    new PIDFeed(instance, 0)
            );

        }
        return instance;
    }

    public void setFeedVel(double vel) {
        this.m_feed.set(ControlMode.Velocity, vel);
    }

    public void setFunnelVel(double vel) {
        this.m_funnel.set(ControlMode.Velocity, vel);
     }

    public double getFunnelSpeed() {
        return this.m_encoder.getNormalizedVelocity();
    }


    @Override
    public void periodic() {
        SmartDashboard.putNumber("Feed Speed:", this.m_encoder.getNormalizedVelocity());
    }
}
