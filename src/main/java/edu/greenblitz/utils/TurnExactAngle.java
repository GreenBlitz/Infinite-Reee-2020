package edu.greenblitz.utils;

import edu.greenblitz.bigRodika.Robot;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;

public class TurnExactAngle {
    private double turn, leftInitialMeters, rightInitialMeters;

    public TurnExactAngle(float turn) {
        this.turn = Chassis.getInstance().getAngle() + turn;
        leftInitialMeters = Chassis.getInstance().getLeftMeters();
        rightInitialMeters = Chassis.getInstance().getRightMeters();
    }

    public void execute() {
        rightP();
    }

    public void rightP() {
        double error = this.turn - Chassis.getInstance().getAngle();
        double norm = error / 1.8;
        Chassis.getInstance().moveMotors(leftP(), norm * RobotMap.BigRodika.Turn.RKp);
    }

    public double leftP() {
        double error = Chassis.getInstance().getRightMeters() - this.base.getLeftTicks();
        return error * RobotMap.BigRodika.Turn.LKp;
    }
}
