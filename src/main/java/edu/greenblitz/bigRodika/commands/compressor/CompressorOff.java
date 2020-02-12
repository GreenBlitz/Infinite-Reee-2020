package edu.greenblitz.bigRodika.commands.compressor;

import edu.greenblitz.bigRodika.subsystems.Pneumatics;

public class CompressorOff extends CompressorCommand {

    @Override
    public void initialize() {
        compressor.setCompressor(false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
