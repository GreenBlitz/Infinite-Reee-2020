package edu.greenblitz.bigRodika.commands.turret.help;

import edu.greenblitz.bigRodika.commands.turret.TurretCommand;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class TurretByTriggers extends TurretCommand {

    private SmartJoystick controllingStick;

    public TurretByTriggers(SmartJoystick stick){
        controllingStick = stick;
    }

    @Override
    public void execute() {
        turret.moveTurret(0.4*(controllingStick.getAxisValue(SmartJoystick.Axis.RIGHT_TRIGGER)
         - controllingStick.getAxisValue(SmartJoystick.Axis.LEFT_TRIGGER)));
    }

    @Override
    public void end(boolean interrupted) {
        turret.moveTurret(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
