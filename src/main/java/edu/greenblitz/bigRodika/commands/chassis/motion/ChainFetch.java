package edu.greenblitz.bigRodika.commands.chassis.motion;

import edu.greenblitz.bigRodika.OI;
import edu.greenblitz.bigRodika.commands.chassis.ArcadeDriveUntilVision;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ChainFetch extends SequentialCommandGroup {

    public ChainFetch(int count, SmartJoystick stick){
        addCommands(
                new ArcadeDriveUntilVision(stick),
                new GoFetch(),
                new GBCommand() {

                    @Override
                    public void initialize() {
                        Chassis.getInstance().putNumber("Balls Collected", count);
                        if (count > 0){
                            new ChainFetch(count - 1, stick).schedule();
                        } else {
                            new GBCommand() {
                                @Override
                            public void initialize() {
                                Chassis.getInstance().toBrake();
                            }
                        }.schedule();
                            new GBCommand() {
                                @Override
                                public void initialize() {
                                    Chassis.getInstance().moveMotors(0,0);
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
