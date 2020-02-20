package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.subsystems.Turret;

public class TurretApproachSwiftlyByRadiansAbsulote extends TurretCommand {


    private double angleToTurnRads;
    private TurretApproachSwiftly cmd;

    public TurretApproachSwiftlyByRadiansAbsulote(double angleToTurnRads){
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
