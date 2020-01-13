package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.gblib.threading.ThreadedCommand;

public class LocalizerCommandRunner extends ThreadedCommand {
    public LocalizerCommandRunner() {
        super(new LocalizerCommand(20));
    }
}
