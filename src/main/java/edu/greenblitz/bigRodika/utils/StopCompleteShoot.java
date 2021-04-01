package edu.greenblitz.bigRodika.utils;

import edu.greenblitz.bigRodika.commands.complex.multisystem.CompleteShootSkills;
import edu.greenblitz.gblib.command.GBCommand;

public class StopCompleteShoot extends GBCommand {

    @Override
    public void initialize() {
        CompleteShootSkills.finished = true;
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
