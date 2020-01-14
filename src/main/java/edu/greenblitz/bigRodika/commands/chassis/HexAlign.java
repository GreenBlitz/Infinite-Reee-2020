package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.commands.chassis.profiling.Follow2DProfileCommand;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.gblib.command.GBCommand;
import edu.greenblitz.gblib.threading.ThreadedCommand;
import org.greenblitz.motion.base.Point;
import org.greenblitz.motion.base.State;
import org.greenblitz.motion.pid.PIDObject;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.ArrayList;
import java.util.List;

public class HexAlign extends GBCommand {

    private Follow2DProfileCommand prof;
    private final double k = 0.5;
    private final double r = 3; //radius
    private Point hex = new Point(1,1);

    @Override
    public void initialize(){
        //vision give me dataaaaaaa
        double[] difference = new double[]{2.5,2,4};//VisionMaster.getInstance().getCurrentVisionData();
        double targetX = difference[0];
        double targetY = difference[2];
        //assume targetY != 0
        double relAng = Math.atan(targetX/targetY);
        double absAng = Chassis.getInstance().getAngle();
        double angle = Math.PI/2 - absAng + relAng - k*Math.asin(Math.sin(-relAng)*targetY/r);
        State endState = new State(hex.getX() + Math.cos(angle), hex.getY() - Math.sin(angle), Math.PI / 2 - angle);
        List<State> path = new ArrayList<>();
        path.add(new State(Chassis.getInstance().getLocation(), Chassis.getInstance().getAngle()));
        path.add(endState);
        prof = new Follow2DProfileCommand(path,
                .0001, 40,
                new ProfilingData(1,2,2.35,6.2),
                0.5, 1, 1,
                new PIDObject(1.5/3.2, 0.02/4.5, 170/4.5), .01,
                new PIDObject(0), .01,
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
