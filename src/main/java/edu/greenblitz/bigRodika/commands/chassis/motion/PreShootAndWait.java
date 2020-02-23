package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PreShootAndWait extends SequentialCommandGroup {

    public PreShootAndWait(PreShoot shoot){

        addCommands(
                shoot,
                new GBCommand() {
                    @Override
                    public boolean isFinished() {
                        return !OI.getInstance().getMainJoystick().A.get();
                    }
                }
        );

    }

}
