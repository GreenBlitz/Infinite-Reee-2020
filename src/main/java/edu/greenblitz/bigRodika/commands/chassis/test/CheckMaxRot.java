package edu.greenblitz.bigRodika.commands.chassis.test;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.debug.RemoteCSVTarget;

public class CheckMaxRot extends GBCommand {

    private double power;
    private double previousAngle;
    private double previousVel;
    private double previousTime;
    private long tStart;
    private RemoteCSVTarget target;
    int count;

    public CheckMaxRot(double power) {
        requires(Chassis.getInstance());
        this.power = power;
    }

    @Override
    public void initialize() {
        previousTime = System.currentTimeMillis() / 1000.0;
        previousAngle = Chassis.getInstance().getRawAngle();
        previousVel = 0;
        count = 0;
        tStart = System.currentTimeMillis();
        target = RemoteCSVTarget.initTarget("RotationalData", "time", "vel", "acc");
    }

    @Override
    public void execute() {
        count++;

        while (System.currentTimeMillis() - tStart < 5000) {
            Chassis.getInstance().moveMotors(-power, power);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double time = System.currentTimeMillis() / 1000.0;
            double angle = Chassis.getInstance().getRawAngle();
            double V = (angle - previousAngle) / (time - previousTime);
            target.report(time - tStart/1000.0, V, (V - previousVel) / (time - previousTime));
            previousAngle = angle;
            previousTime = time;
            previousVel = V;


        }
    }

    @Override
    public void end(boolean inter){
        System.out.println(System.currentTimeMillis() - tStart);
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - tStart > 5000;
    }
}
