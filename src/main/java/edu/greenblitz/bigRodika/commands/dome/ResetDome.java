package edu.greenblitz.bigRodika.commands.dome;


public class ResetDome extends DomeMoveByConstant {

    private static final double DEFAULT_POWER = 0.3;

    private int count;
    private long tStart = 0;
    private static final long TIMEOUT = 3000;

    public ResetDome() {
        this(DEFAULT_POWER);
    }

    public ResetDome(double power) {
        super(-Math.abs(power));
    }

    @Override
    public void initialize() {
        super.initialize();
        tStart = System.currentTimeMillis();
        count = 0;
    }

    @Override
    public void end(boolean interrupted) {
        if (!interrupted) {
            dome.safeMove(0);
        }
    }

    @Override
    public void execute() {
        super.execute();
        if (dome.switchTriggered() && dome.getRawTicks() < 1) {
            count += 1;
        }
    }

    @Override
    public boolean isFinished() {
        return count > 5 || (System.currentTimeMillis() - tStart > TIMEOUT);
    }
}
