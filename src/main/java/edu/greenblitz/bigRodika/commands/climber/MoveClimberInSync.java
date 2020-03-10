package edu.greenblitz.bigRodika.commands.climber;

import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.greenblitz.motion.pid.PIDObject;

public class MoveClimberInSync extends ParallelCommandGroup {
    public MoveClimberInSync(SmartJoystick joystick, double mult, PIDObject obj) {
        addCommands(new ClimbByTriggers(joystick, mult), new HookByPID(obj));
    }
}
