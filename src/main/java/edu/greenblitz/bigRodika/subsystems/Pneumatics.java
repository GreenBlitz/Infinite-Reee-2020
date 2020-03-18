package edu.greenblitz.bigRodika.subsystems;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.compressor.HandleCompressor;
import edu.wpi.first.wpilibj.Compressor;

public class Pneumatics extends GBSubsystem {
    private static Pneumatics instance;

//    private edu.greenblitz.gblib.sensors.PressureSensor m_pressureSensor;
    private Compressor m_compressor;

    private Pneumatics() {

//        m_pressureSensor = new edu.greenblitz.gblib.sensors.PressureSensor(RobotMap.Limbo2.Pneumatics.PressureSensor.PRESSURE);
        m_compressor = new Compressor(RobotMap.Limbo2.Pneumatics.PCM);


    }

    public static void init() {
        if (instance == null) {
            instance = new Pneumatics();
//            instance.setDefaultCommand(new HandleCompressor());
        }
    }

    public static Pneumatics getInstance() {
        return instance;
    }

    public double getPressure() {
        return 999;
//        return m_pressureSensor.getPressure();
    }

    public void setCompressor(boolean compress) {
        if (compress) {
            m_compressor.start();
        } else {
            m_compressor.stop();
        }

    }

    public boolean isEnabled() {
        return m_compressor.enabled();
    }

    public void reset() {
        setCompressor(false);
    }
}