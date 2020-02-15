package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.commands.chassis.ArcadeDriveUntilVision;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.greenblitz.motion.base.State;

public class ChainFetch extends SequentialCommandGroup {

    public ChainFetch(int count, SmartJoystick stick){
        Chassis.getInstance().putNumber("Balls Collected", count);
        VisionMaster.Algorithm.POWER_CELLS.setAsCurrent();
        addCommands(
                new ArcadeDriveUntilVision(stick),
                new GoFetch(),
                new GBCommand() {


                    @Override
                    public void initialize() {
                        if (count > 0){
                            new ChainFetch(count - 1, stick).schedule();
                        } else {
                            new GBCommand() {
                                @Override
                            public void initialize() {
                                Chassis.getInstance().toBrake();
                            }
                        }.schedule();
                        }
                    }

                    @Override
                    public boolean isFinished() {
                        return true;
                    }
                }
        );

    }

}
