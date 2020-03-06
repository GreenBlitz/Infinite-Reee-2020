package edu.greenblitz.bigRodika.commands.turret.profiling;

import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.gblib.threading.IThreadable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Position;

import java.util.function.Supplier;

public class DelicateTurnTurret implements IThreadable {

    private double POWER = 0.07;
    private double TOL = Math.toRadians(1.5);
    private Supplier<Double> goalSupp;
    private double goal;
    private Turret turret;
    private double mult;

    public DelicateTurnTurret(Supplier<Double> goal) {
        this.goalSupp = goal;
        this.turret = Turret.getInstance();
    }


    @Override
    public void run() {
        if (isFinished()){
            turret.moveTurret(0);
            return;
        }
        turret.moveTurret(POWER * mult);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(Position.normalizeAngle(
                turret.getNormAngleRads() - goal)) <= TOL;
    }

    @Override
    public void atEnd() {
        double err = Math.toDegrees(Position.normalizeAngle(turret.getNormAngleRads() - goal));
        SmartDashboard.putNumber("Final Final Error", err);

        turret.moveTurret(0);
    }

    @Override
        public void atInit() {
        goal = goalSupp.get();
        mult = Math.signum(Position.normalizeAngle(goal - turret.getNormAngleRads()));
    }
}
