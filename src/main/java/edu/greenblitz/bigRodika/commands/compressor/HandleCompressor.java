package edu.greenblitz.bigRodika.commands.compressor;

import edu.greenblitz.bigRodika.subsystems.Pneumatics;
import edu.greenblitz.gblib.command.GBCommand;

public class HandleCompressor extends GBCommand {

    private Pneumatics pn;

    public HandleCompressor(Pneumatics pn) {
        super(pn);
        this.pn = pn;
    }


    @Override
    public void execute() {
        if (false) {
            if (pn.getPressure() < 65) {
                pn.setCompressor(true);
            }
            if (pn.getPressure() > 100) {
                pn.setCompressor(false);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}