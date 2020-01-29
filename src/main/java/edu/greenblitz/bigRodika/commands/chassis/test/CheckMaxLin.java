package edu.greenblitz.bigRodika.commands.chassis.test;

import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.debug.RemoteCSVTarget;

public class CheckMaxLin extends ChassisCommand {

    private double power;
    private double previousVel;
    private double previousTime;
    private RemoteCSVTarget target;
    private long tStart;
    int count;

    public CheckMaxLin(double power) {
        this.power = power;
    }

    @Override
    public void initialize() {
        previousTime = System.currentTimeMillis() / 1000.0;
        previousVel = 0;
        count = 0;
        tStart = System.currentTimeMillis();
        target = RemoteCSVTarget.initTarget("LinearData", "time", "vel", "acc");
    }

    @Override
    public void execute() {
        count++;

        while (System.currentTimeMillis() - tStart < 5000) {
            Chassis.getInstance().moveMotors(power, power);

            double time = System.currentTimeMillis() / 1000.0;
            double V = Chassis.getInstance().getLinearVelocity();
            target.report(time - tStart/1000.0, V, (V - previousVel) / (time - previousTime));
            previousTime = time;
            previousVel = V;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - tStart > 3000;
    }
}
