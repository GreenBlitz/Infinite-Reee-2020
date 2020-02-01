package edu.greenblitz.bigRodika.commands.chassis.turns;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.IThreadable;
import org.greenblitz.motion.base.Position;

public class DelicateTurn implements IThreadable {

    private double POWER = 0.2;
    private double TOL = Math.toRadians(1);
    private double goal;
    private double rightMult;

    public DelicateTurn(double goal){
        this.goal = goal;
    }


    @Override
    public void run() {
        Chassis.getInstance().moveMotors(POWER * -rightMult, POWER * rightMult);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(Position.normalizeAngle(Chassis.getInstance().getAngle() - goal)) <= TOL;
    }

    @Override
    public void atEnd() {
        Chassis.getInstance().moveMotors(0, 0);
    }

    @Override
    public void atInit() {
        rightMult = -Math.signum(Position.normalizeAngle(goal - Chassis.getInstance().getLocation().getAngle()));
        Chassis.getInstance().toBrake();
    }
}
