package edu.greenblitz.utils;

import edu.greenblitz.bigRodika.Robot;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Chassis;

public class TurnExactAngle {
    private double turn;
    private Chassis base;

    public TurnExactAngle(float turn, Chassis base) {
        this.base = base;
        this.turn = this.base.getYTurn() + turn;
    }

    public void execute() {
        rightP();
    }

    public void rightP() {
        double error = this.base.getYTurn() - this.turn;
        double norm = error / 3.6;
        this.base.tankDrive(leftP(), norm * RobotMap.BigRodika.Turn.RKp);
    }

    public double leftP() {
        double error = this.base.getRightTicks() - this.base.getLeftTicks();
        return error * RobotMap.BigRodika.Turn.LKp;
    }
}
