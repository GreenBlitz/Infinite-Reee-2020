package edu.greenblitz.bigRodika.commands.chassis.driver;

import edu.greenblitz.bigRodika.commands.chassis.ChassisCommand;
import edu.greenblitz.gblib.gears.GearDependentValue;
import edu.greenblitz.gblib.hid.SmartJoystick;

public class ArcadeDrive extends ChassisCommand {

    private SmartJoystick joystick;
    private GearDependentValue<Double> forwardsFactor = new GearDependentValue<>(1.0, 1.0);
    private GearDependentValue<Double> turnFactor = new GearDependentValue<>(
            0.55,
            0.55);

    public ArcadeDrive(SmartJoystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public void initialize() {
        chassis.toBrake();
    }

    @Override
    public void execute() {
        chassis.arcadeDrive(joystick.getAxisValue(SmartJoystick.Axis.LEFT_Y)*forwardsFactor.getValue(),
                joystick.getAxisValue(SmartJoystick.Axis.RIGHT_X)*turnFactor.getValue());
    }
}
