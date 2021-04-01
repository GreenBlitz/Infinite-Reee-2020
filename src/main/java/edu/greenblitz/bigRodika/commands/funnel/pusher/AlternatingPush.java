package edu.greenblitz.bigRodika.commands.funnel.pusher;

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
        startTime = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        double timeNow = (System.currentTimeMillis() - startTime) / 1000.0;
        double timeNowMod = timeNow % (timeIn + timeOut);
        if (timeNowMod < timeIn) {
            funnel.movePusher(powerIn, 0.5 * powerIn);
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
