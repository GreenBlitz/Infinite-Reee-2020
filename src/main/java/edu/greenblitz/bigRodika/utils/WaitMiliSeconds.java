package edu.greenblitz.bigRodika.utils;

import edu.wpi.first.wpilibj2.command.WaitCommand;

public class WaitMiliSeconds extends WaitCommand {

    public WaitMiliSeconds(long time){
        super(time / 1000.0);
    }

}
