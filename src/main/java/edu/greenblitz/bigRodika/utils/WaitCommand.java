package edu.greenblitz.bigRodika.utils;

public class WaitCommand extends GBCommand {

    private double timeout;
    private long tStart;


    public WaitCommand(double timeout) {
        this.timeout = timeout; // Wait period in milliseconds.
        this.tStart = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - tStart >= timeout;
    }
}