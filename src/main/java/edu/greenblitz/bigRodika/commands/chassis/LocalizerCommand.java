package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.threading.IThreadable;
import org.greenblitz.motion.app.Localizer;

public class LocalizerCommand implements IThreadable {

    private Chassis chassis;
    private Localizer localizer;
    private long minimumLoopTime;
    private long tPrev;
    private boolean died;

    private static final long DEFAULT_MINIMUM_LOOP_TIME = 0;

    public LocalizerCommand(){
        this(DEFAULT_MINIMUM_LOOP_TIME);
    }

    public LocalizerCommand(long t){
        minimumLoopTime = t;
    }

    @Override
    public void run() {
        localizer.update(chassis.getLeftMeters(), chassis.getRightMeters(), chassis.getAngle());
        if (System.currentTimeMillis() - tPrev < minimumLoopTime) {
            try {
                Thread.sleep(minimumLoopTime - (System.currentTimeMillis() - tPrev));
            } catch (InterruptedException e) {
                e.printStackTrace();
                died = true;
            }
        }
        tPrev = System.currentTimeMillis();
    }

    @Override
    public boolean isFinished() {
        return died;
    }

    @Override
    public void atEnd() { }

    @Override
    public void atInit() {
        died = false;
        chassis = Chassis.getInstance();
        chassis.resetGyro();
        localizer = Localizer.getInstance();
        localizer.configure(chassis.getWheelDistance(), 0, 0);
        localizer.reset(chassis.getLeftMeters(), chassis.getRightMeters());
        tPrev = System.currentTimeMillis();
    }

}
