package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.commands.dome.ResetDome;
import edu.greenblitz.bigRodika.subsystems.Intake;
import edu.greenblitz.gblib.command.GBCommand;

public class TrenchPrepare extends GBCommand {

    private static final double DOME_CLOSE_POWER = -0.3;

    public TrenchPrepare() {
        require(Intake.getInstance().getExtender());
    }

    @Override
    public void initialize() {
        Intake.getInstance().getExtender().extend();
        new ResetDome(DOME_CLOSE_POWER).schedule();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
