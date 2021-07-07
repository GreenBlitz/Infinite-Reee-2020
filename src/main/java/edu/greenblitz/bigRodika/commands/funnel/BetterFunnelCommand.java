package edu.greenblitz.bigRodika.commands.funnel;

import edu.greenblitz.bigRodika.commands.funnel.inserter.InsertByConstant;
import edu.greenblitz.bigRodika.commands.funnel.pusher.PushByDifferentConstants;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;

public class BetterFunnelCommand extends GBCommand {
    private double targetSpeed;
    public static final double deltaLimit = 50; // arbitrary limit TODO: calibrate limit

    private PushByDifferentConstants push;
    private InsertByConstant insert;

    public BetterFunnelCommand(double wantedSpeed) {
        this.targetSpeed = wantedSpeed;
        this.push = new PushByDifferentConstants(0.8, -0.3);
        this.insert = new InsertByConstant(0.6);
    }

    @Override
    public void initialize() {
        this.push.initialize();
        this.insert.initialize();
    }

    @Override
    public void execute() {
        if(Math.abs(Shooter.getInstance().getShooterSpeed() - this.targetSpeed) > deltaLimit) {
            push.execute();
            insert.execute();
        } else {
            push.end(true);
            insert.end(true);

            this.push = new PushByDifferentConstants(0.8, -0.3);
            this.insert = new InsertByConstant(0.6);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        this.push.end(interrupted);
        this.insert.end(interrupted);
    }
}
