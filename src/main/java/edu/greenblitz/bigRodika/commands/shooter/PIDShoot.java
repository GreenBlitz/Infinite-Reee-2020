package edu.greenblitz.bigRodika.commands.shooter;

import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.List;

public class PIDShoot extends GBCommand {
    private double vel;
    private double acc, lastVel;
    private List<Double> speeds;

    private static final double Kp = 0.1, Ki = 0.01, Kd = 0.001, Ff = 0.5, y0 = 0.2, angle = 45, y = 2, xDiff = 0.3, yMin = 1.7;
    private int end1, end2;
    public static double tempX = 5;//will be done with vision
    private boolean Do = true;

    public PIDShoot(Shooter s) {
        super(s);
        if (!(calculateShootSpeed(tempX, y, y0, angle) == 0)) {
            SmartDashboard.putString("Shoot viable", "True");
            if (function(tempX - xDiff, calculateShootSpeed(tempX, y, y0, angle), angle, y0) < yMin + 0.3) {
                this.vel = calculateShootSpeed(tempX - xDiff, y, y0, angle);
                this.vel = calculateShootSpeed(tempX, y, y0, angle);
            } else {
                this.vel = calculateShootSpeed(tempX, y, y0, angle);
            }
        } else {
            SmartDashboard.putString("Shoot viable", "False");
            this.Do = false;
        }
        this.speeds = new ArrayList<Double>();
    }

    @Override
    public void execute() {
        if (this.Do) {
            this.speeds.add(Shooter.getInstance().getShooterSpeed());
            if (this.vel - Shooter.getInstance().getShooterSpeed() < 0.1) {
                if (this.recentSum() < 0.2) {
                    stage3();
                } else {
                    stage2();
                }
            } else {
                stage1();
            }
        }
    }

    public void stage1() {
        if (this.vel - Shooter.getInstance().getShooterSpeed() < 0.1) {
            this.end1 = this.speeds.size() - 1;

        }
    }

    public void stage2() {
        double sum = totalSum();
        double error = this.vel - Shooter.getInstance().getShooterSpeed();
        double out = error * Kp + sum * Ki + Ff;
        Shooter.getInstance().shoot(out);
        if (this.recentSum() < 0.2) {
            this.end2 = this.speeds.size() - 1;
        }

    }

    public void stage3() {
        this.acc = Shooter.getInstance().getShooterSpeed() - this.lastVel;
        this.lastVel = Shooter.getInstance().getShooterSpeed();
        double error = this.vel - Shooter.getInstance().getShooterSpeed();
        double d = this.acc;
        double out = error * Kp + d * Kd + stage2Int() * Ki + Ff;
        Shooter.getInstance().shoot(1);
        Shooter.getInstance().shoot(out);
    }

    public double recentSum() {
        double sum = 0;
        for (int i = 1; i <= Math.min(this.speeds.size(), 10); i++) {
            sum += this.speeds.get(this.speeds.size() - i);
        }
        return sum;
    }

    public double totalSum() {
        double sum = 0;
        for (int i = end1; i < this.speeds.size(); i++) {
            sum += this.speeds.get(i);
        }

        return sum;
    }

    public double stage2Int() {
        double sum = 0;
        for (int i = end1; i <= end2; i++) {
            sum += this.speeds.get(i);
        }
        return sum;
    }

    public double calculateShootSpeed(double x, double y, double y0, double angle) {
        double v0 = 4.905 * x * x;
        v0 /= Math.cos(angle);
        v0 /= Math.cos(angle);
        v0 /= y0 + Math.tan(angle) * x - y;
        if (v0 > 0) {
            return Math.sqrt(v0);
        } else {
            return 0;
        }
    }

    public double function(double x, double v0, double angle, double y0) {
        double y = y0 + Math.tan(angle) * x - 4.905 * x * x / Math.cos(angle) / Math.cos(angle) / v0 / v0;
        return y;
    }

}
