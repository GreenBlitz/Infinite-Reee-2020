package edu.greenblitz.bigRodika.commands.turret;


public class TurretApproachSwiftlyByRadiansRelative extends TurretCommand {


    private double angleToTurnRads;
    private TurretApproachSwiftly cmd;

    public TurretApproachSwiftlyByRadiansRelative(double angleToTurnRads){
        super();
        this.angleToTurnRads = angleToTurnRads;
    }

    @Override
    public void initialize(){
        cmd = new TurretApproachSwiftly((angleToTurnRads)/(2*Math.PI) + turret.getTurretLocation());
        cmd.initialize();
    }

    @Override
    public void execute(){
        cmd.execute();
    }

    @Override
    public boolean isFinished(){
        return cmd.isFinished();
    }

    @Override
    public void end(boolean interrupted){
        cmd.end(interrupted);
    }


}
