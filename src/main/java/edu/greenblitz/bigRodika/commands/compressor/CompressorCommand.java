package edu.greenblitz.bigRodika.commands.compressor;

import edu.greenblitz.bigRodika.subsystems.Pneumatics;
import edu.greenblitz.gblib.command.GBCommand;

public abstract class CompressorCommand extends GBCommand {

    protected Pneumatics compressor;

    public CompressorCommand(Pneumatics cmprs){
        super(cmprs);
        compressor = cmprs;
    }

}
