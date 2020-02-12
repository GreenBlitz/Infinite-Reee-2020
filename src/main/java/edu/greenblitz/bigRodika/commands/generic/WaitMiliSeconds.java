package edu.greenblitz.bigRodika.commands.generic;

import edu.wpi.first.wpilibj2.command.WaitCommand;

public class WaitMiliSeconds extends WaitCommand {

    public WaitMiliSeconds(long time){
        super(time / 1000.0);
    }

}
