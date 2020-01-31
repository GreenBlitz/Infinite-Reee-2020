package edu.greenblitz.bigRodika.commands.generic;

import edu.greenblitz.gblib.command.GBCommand;

public class WaitMiliSeconds extends GBCommand {
    private long stime;
    private long wtime;

    public WaitMiliSeconds(long time){
        wtime = time;
    }

    @Override
    public void initialize(){
        stime = System.currentTimeMillis();
    }

    @Override
    public boolean isFinished(){
        return stime + wtime >= System.currentTimeMillis();
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Finished waiting");
    }
}
