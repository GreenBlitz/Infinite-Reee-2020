package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class HexAlign extends GBCommand {

    private Follow2DProfileCommand prof;
    private final double k = 0.5;
    private final double r = 0.6; //radius

    @Override
    public void initialize(){
        State startState = new State(Chassis.getInstance().getLocation(), -Chassis.getInstance().getAngle());
        //vision give me dataaaaaaa
        double[] difference = VisionMaster.getInstance().getVisionLocation().toDoubleArray();//VisionMaster.getInstance().getCurrentVisionData();
        double targetX = difference[0];
        double targetY = difference[2];
        //assume targetY != 0
        double relAng = Math.atan(targetX/targetY);
        double absAng = Chassis.getInstance().getAngle();

        Point hex = new Point(targetX*Math.cos(absAng) - targetY*Math.sin(absAng) + startState.getX(),targetY*Math.cos(absAng) + targetX*Math.sin(absAng) + startState.getY());
        SmartDashboard.putString("hex", hex.toString());

        double angle = (Math.abs(Math.sin(-relAng)*targetY/r) > 1) ? Math.PI/2 - absAng + relAng : Math.PI/2 - absAng + relAng - k*Math.asin(Math.sin(-relAng)*targetY/r);
        State endState = new State(hex.getX() + r*Math.cos(angle), hex.getY() - r*Math.sin(angle), -(Math.PI / 2 - angle));

        List<State> path = new ArrayList<>();
        path.add(startState);
        path.add(endState);

        endState.setAngle(Math.toDegrees(endState.getAngle()));
        SmartDashboard.putString("end", endState.toString());
        endState.setAngle(Math.toRadians(endState.getAngle()));

        prof = new Follow2DProfileCommand(path,
                .001, 1000,
                RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.7"),
                0.7, 1, 1,
                new PIDObject(0, 0, 0), .01,
                new PIDObject(0, 0, 0), .01,
                false);
    }

    @Override
    public void execute(){
    }

    @Override
    public void end(boolean interupted) {
        new ThreadedCommand(prof, Chassis.getInstance()).schedule();

    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
