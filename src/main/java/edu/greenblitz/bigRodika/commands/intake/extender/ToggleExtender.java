package edu.greenblitz.bigRodika.commands.intake.extender;

public class ToggleExtender extends ExtenderCommand {

    @Override
    public void initialize() {
        intake.toggleExtender();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
