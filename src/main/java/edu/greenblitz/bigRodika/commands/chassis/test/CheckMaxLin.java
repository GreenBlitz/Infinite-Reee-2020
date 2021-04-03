package edu.greenblitz.bigRodika.commands.chassis.test;

import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import org.greenblitz.debug.RemoteCSVTarget;
import org.greenblitz.debug.RemoteCSVTargetBuffer;

//TODO: add CSV
public class CheckMaxLin extends ChassisCommand {

    int count;
    private double power;
    private RemoteCSVTargetBuffer target;
    private double previousVel;
    private double previousTime;
    private long tStart;

    public CheckMaxLin(double power) {
        this.power = power;
    }

    @Override
    public void initialize() {
        target = new RemoteCSVTargetBuffer("LinearData", "time", "vel", "acc");
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

        double A = (V-previousVel)/(time-previousTime);
        target.report(time -  tStart/1000.0, V, A);

        previousTime = time;
        previousVel = V;

    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - tStart > 3000;
    }

    @Override
    public void end(boolean interrupted){
        target.passToCSV(true);
    }
}
