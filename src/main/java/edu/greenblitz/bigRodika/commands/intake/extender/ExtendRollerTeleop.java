package edu.greenblitz.bigRodika.commands.intake.extender;

public class ExtendRollerTeleop extends ExtenderCommand {
    @Override
    public void initialize() {

        intake.extendSpecial();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
