package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.gblib.threading.IThreadable;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.greenblitz.motion.app.Localizer;

public class ThreadedCommandLocalizer extends ThreadedCommand {

    private Localizer l;

    public ThreadedCommandLocalizer(IThreadable func, Subsystem... req) {
        super(func, req);
        this.l = Localizer.getInstance();
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println(l.getLocation());
    }
}
