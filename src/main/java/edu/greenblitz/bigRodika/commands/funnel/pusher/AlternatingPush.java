package edu.greenblitz.bigRodika.commands.funnel.pusher;

import edu.greenblitz.bigRodika.OI;

public class AlternatingPush extends PusherCommand {

    private int number;
    private long startTime;
    private long time;
    private double powerIn;
    private double powerOut;
    private double timeIn;
    private double timeOut;

    public AlternatingPush(double powerIn, double powerOut, double timeIn, double timeOut, int number) {
        super();
        this.number = number; //we dont use this for now
        this.powerIn = powerIn;
        this.powerOut = powerOut;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    @Override
    public void initialize() {
        funnel.movePusher(powerOut);
        startTime = System.currentTimeMillis() + 500;
    }

    @Override
    public void execute() {
        double timeNow = (System.currentTimeMillis() - startTime) / 1000.0;
        double timeNowMod = (timeNow + timeIn + timeOut) % (timeIn + timeOut);//this is not because I stupid
        // adding to startTime makes timeNow negative and modulo keeps it neg cause java is dumb
        if (timeNowMod < timeIn || OI.getInstance().pusherAlternate.get()) {
            funnel.movePusher(0.5 *powerIn, powerIn);
        } else {
            funnel.movePusher(powerOut);
        }

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        funnel.movePusher(0);
    }
}
