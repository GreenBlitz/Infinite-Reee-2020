package edu.greenblitz.bigRodika.commands.vision;

import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;

public class SwitchVisionAlgorithm extends GBCommand {
    private final VisionMaster.Algorithm algorithm;

    public SwitchVisionAlgorithm(VisionMaster.Algorithm algo){
        this.algorithm = algo;
    }

    @Override
    public void initialize() {
        algorithm.setAsCurrent();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
