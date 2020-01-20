package edu.greenblitz.bigRodika.subsystems;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.compressor.HandleCompressor;
import edu.greenblitz.gblib.sensors.PressureSensor;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Pneumatics implements Subsystem {
    private static Pneumatics instance;

    private PressureSensor m_pressureSensor;
    private Compressor m_compressor;
    private boolean m_activated;

    private Pneumatics() {

        m_pressureSensor = new PressureSensor(RobotMap.BigRodika.Pneumatics.Sensor.PRESSURE);
        m_compressor = new Compressor(RobotMap.BigRodika.Pneumatics.PCM);

        setDefaultCommand(new HandleCompressor(this));

    }

    public double getPressure() {
        return m_pressureSensor.getPressure();
    }

    public void setCompressor(boolean compress) {
        if (compress) {
            m_compressor.start();
        } else {
            m_compressor.stop();
        }

        m_activated = compress;
    }

    public boolean isEnabled() {
        return m_compressor.enabled();
    }

    public static void init() {
        if (instance == null)
            instance = new Pneumatics();
    }

    public static Pneumatics getInstance() {
        if (instance == null) instance = new Pneumatics();
        return instance;
    }

    public void reset() {
        setCompressor(false);
    }
}