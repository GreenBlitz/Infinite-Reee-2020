package edu.greenblitz.bigRodika.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.gblib.encoder.IEncoder;
import edu.greenblitz.gblib.encoder.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Turret implements Subsystem {
    private static Turret instance;

    public static void init(){
        if(instance == null) {
            instance = new Turret();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
    }

    public static Turret getInstance() {
        return instance;
    }

    private WPI_TalonSRX motor;
    private IEncoder encoder;
    private DigitalInput microSwitch;

    private Turret(){
        motor = new WPI_TalonSRX(RobotMap.BigRodika.Turret.MOTOR_PORT);
        encoder = new TalonEncoder(RobotMap.BigRodika.Turret.NORMALIZER, motor);
        microSwitch = new DigitalInput(RobotMap.BigRodika.Turret.SWITCH_PORT);
        new Thread(()->{
            while (true){
                if (isSwitchPressed()){
                    encoder.reset();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void moveTurret(double power){
        motor.set(power);
    }

    public double getSpeed(){
        return (encoder.getNormalizedVelocity());
    }

    public boolean isSwitchPressed(){
        return microSwitch.get();
    }

}
