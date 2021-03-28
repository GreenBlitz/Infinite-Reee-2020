package edu.greenblitz.bigRodika.commands.chassis.test;

import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import org.greenblitz.debug.RemoteCSVTarget;

//TODO: add CSV
public class CheckMaxLin extends ChassisCommand {

    int count;
    private double power;
    private double previousVel;
    private double previousTime;
    private long tStart;

    public CheckMaxLin(double power) {
        this.power = power;
    }

    @Override
    public void initialize() {
        previousTime = System.currentTimeMillis() / 1000.0;
        previousVel = 0;
        count = 0;
        tStart = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        count++;

        Chassis.getInstance().moveMotors(power, power);

        double time = System.currentTimeMillis() / 1000.0;
        double V = Chassis.getInstance().getLinearVelocity();
        previousTime = time;
        previousVel = V;

    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - tStart > 3000;
    }
}
