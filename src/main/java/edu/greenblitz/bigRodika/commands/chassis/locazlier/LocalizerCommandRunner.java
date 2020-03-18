package edu.greenblitz.bigRodika.commands.chassis.locazlier;

import edu.greenblitz.gblib.threading.ThreadedCommand;

public class LocalizerCommandRunner extends ThreadedCommand {
    public LocalizerCommandRunner() {
        super(new LocalizerCommand(20));
    }
}
