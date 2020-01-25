package edu.greenblitz.bigRodika.commands.compressor;

import edu.greenblitz.bigRodika.subsystems.Pneumatics;
import edu.greenblitz.gblib.command.GBCommand;

public class HandleCompressor extends CompressorCommand {

    private static final int TURN_ON_THRESHOLD = 65, TURN_OFF_THRESHOLD = 100;

    public HandleCompressor(Pneumatics pneumatics) {
        super(pneumatics);
    }

    @Override
    public void execute() {
        if (compressor.getPressure() < TURN_ON_THRESHOLD) {
            compressor.setCompressor(true);
        }
        if (compressor.getPressure() > TURN_OFF_THRESHOLD) {
            compressor.setCompressor(false);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}