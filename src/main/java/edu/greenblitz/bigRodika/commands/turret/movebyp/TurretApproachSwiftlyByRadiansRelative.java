package edu.greenblitz.bigRodika.commands.turret.movebyp;


import edu.greenblitz.bigRodika.commands.turret.TurretCommand;

public class TurretApproachSwiftlyByRadiansRelative extends TurretCommand {


    protected double angleToTurnRads;
    private TurretApproachSwiftly cmd;

    public TurretApproachSwiftlyByRadiansRelative(double angleToTurnRads) {
        super();
        this.angleToTurnRads = angleToTurnRads;
    }

    @Override
    public void initialize() {
        cmd = new TurretApproachSwiftly((angleToTurnRads) / (2 * Math.PI) + turret.getTurretLocation());
        cmd.initialize();
    }

    @Override
    public void execute() {
        cmd.execute();
    }

    @Override
    public boolean isFinished() {
        return cmd.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        cmd.end(interrupted);
    }


}
