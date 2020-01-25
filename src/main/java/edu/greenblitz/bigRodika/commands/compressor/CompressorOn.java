package edu.greenblitz.bigRodika.commands.compressor;

import edu.greenblitz.bigRodika.subsystems.Pneumatics;

public class CompressorOn extends CompressorCommand {
    public CompressorOn(Pneumatics cmprs) {
        super(cmprs);
    }

    @Override
    public void initialize() {
        compressor.setCompressor(true);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
