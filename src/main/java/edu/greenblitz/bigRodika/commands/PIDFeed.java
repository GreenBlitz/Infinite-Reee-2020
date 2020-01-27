package edu.greenblitz.bigRodika.commands;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Funnel;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;

import java.util.ArrayList;
import java.util.List;

public class PIDFeed extends GBCommand {
    private double vel;
    private double acc, lastVel;
    private List<Double> speeds;

    private static final double Kp = 0.1, Ki = 0.01, Kd = 0.001, Ff = 9000;
    private int end1, end2;

    public PIDFeed(Funnel funnel, double vel) {
        super(funnel);
        this.vel = vel;
        this.speeds = new ArrayList<Double>();
    }

    @Override
    public void execute() {
        this.speeds.add(Funnel.getInstance().getFunnelSpeed());
        if(this.vel - Funnel.getInstance().getFunnelSpeed() < 0.1) {
            if(this.recentSum() < 0.2) {
                stage3();
            } else {
                stage2();
            }
        } else {
            stage1();
        }
    }

    public void stage1() {
        if(this.vel - Funnel.getInstance().getFunnelSpeed() < 0.1) {
            this.end1 = this.speeds.size() - 1;

        }
    }

    public void stage2() {
        double sum = totalSum();
        double error = this.vel - Funnel.getInstance().getFunnelSpeed();
        double out = error * Kp + sum * Ki + Ff;
        Funnel.getInstance().setFeedVel(out);
        if(this.recentSum() < 0.2) {
            this.end2 = this.speeds.size() - 1;
        }

    }

    public void stage3() {
        this.acc = Funnel.getInstance().getFunnelSpeed() - this.lastVel;
        this.lastVel = Funnel.getInstance().getFunnelSpeed();
        double error = this.vel - Funnel.getInstance().getFunnelSpeed();
        double d = this.acc;
        double out = error * Kp + d * Kd + stage2Int() * Ki + Ff;
        Funnel.getInstance().setFunnelVel(1);
        Funnel.getInstance().setFeedVel(out);
    }

    public double recentSum() {
        double sum = 0;
        for(int i = 1; i <= Math.min(this.speeds.size(), 10); i++) {
            sum += this.speeds.get(this.speeds.size() - i);
        }
        return sum;
    }

    public double totalSum() {
        double sum = 0;
        for(int i = end1; i < this.speeds.size(); i++) {
            sum += this.speeds.get(i);
        }

        return sum;
    }

    public double stage2Int() {
        double sum = 0;
        for(int i = end1; i <= end2; i++) {
            sum += this.speeds.get(i);
        }
        return sum;
    }

}
