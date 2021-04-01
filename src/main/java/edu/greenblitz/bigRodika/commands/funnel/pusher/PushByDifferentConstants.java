package edu.greenblitz.bigRodika.commands.funnel.pusher;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.subsystems.Funnel;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class PushByDifferentConstants extends PusherCommand{

    private double powerLeft, powerRight;

    public PushByDifferentConstants(double powerLeft, double powerRight) {
        super();

        this.powerLeft = powerLeft;
        this.powerRight = powerRight;
    }

    @Override
    public void execute() {
        Funnel.getInstance().movePusher(this.powerLeft, this.powerRight);
    }

    @Override
    public void end(boolean interrupted) {
        Funnel.getInstance().movePusher(0, 0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
