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
    private ThreadedCommand cmd;
    private double k = 0.2;
    private double r = 2.5; //radius
    private Point hexPos;
    private boolean fucked = false;
    private double driveTolerance = 1;
    private double tolarance = 0.05;
    private List<Double> radsAndCritPoints;//crit point - radius - crit - radius - crit .... - radius

    public HexAlign(double r, double k){
        this.k = k;
        this.r = r;
    }

    public HexAlign(List<Double> radsAndCritPoints, double k, double driveTolerance){
        this.radsAndCritPoints = radsAndCritPoints;
        this.k = k;
        this.driveTolerance = driveTolerance;
    }

    public HexAlign(){
    }

    public Point getHexPos(){
        return hexPos;
    }

    @Override
    public void initialize(){
        State startState = new State(Chassis.getInstance().getLocation(), -Chassis.getInstance().getAngle());
        VisionMaster.Algorithm.HEXAGON.setAsCurrent();

        double[] difference = VisionMaster.getInstance().getVisionLocation().toDoubleArray();

        if(!VisionMaster.getInstance().isLastDataValid()) {
            fucked = true;
            return;
        }

        double radCenter = Math.sqrt(Math.pow(difference[0] + RobotMap.BigRodika.Chassis.VISION_CAM_X_DIST_CENTER,2) + Math.pow(difference[1] + RobotMap.BigRodika.Chassis.VISION_CAM_Y_DIST_CENTER,2));

        if(radsAndCritPoints != null) {
            if(radCenter < radsAndCritPoints.get(0)){
                fucked = true;
                return;
            }
            r = radsAndCritPoints.get(radsAndCritPoints.size() - 1);
            for(int i = 0; i < radsAndCritPoints.size() - 1; i ++){
                if(radCenter < radsAndCritPoints.get(i + 1) && i >= radsAndCritPoints.get(i)){
                    r  = radsAndCritPoints.get((i + 1) - i % 2);
                    break;
                }
            }
        }

        double desRadCenter = r + RobotMap.BigRodika.Chassis.VISION_CAM_Y_DIST_CENTER;//TODO This is inaccurate, if cam is not in the middle of the X dir of the robot we are screwed
        double errRadCenter = Math.abs(radCenter - desRadCenter);

        SmartDashboard.putNumber("errRadCenter" , errRadCenter);
        //can be done without all of this definitions, just so the code would be readable

        if(errRadCenter < tolarance) {
            fucked = true;
            return;
        }

        if(errRadCenter < driveTolerance){
            k = 1;
        }

        double targetX = difference[0];
        double targetY = difference[1];
        //assume targetY != 0
        double relAng = Math.atan(targetX/targetY);
        double absAng = Chassis.getInstance().getAngle();

        hexPos = new Point(targetX*Math.cos(absAng) - targetY*Math.sin(absAng) + startState.getX(),targetY*Math.cos(absAng) + targetX*Math.sin(absAng) + startState.getY());

        SmartDashboard.putString("hex", hexPos.toString());
        System.err.println("hex " + hexPos.toString());

        double devConst = 1.5;
        double angle = (Math.abs(targetX) > r) ? ((targetX < 0) ? (1 - k/devConst)*(Math.PI/2 - absAng + relAng): (1 + k/devConst)*(Math.PI/2 - absAng + relAng) - Math.PI * k/devConst)
                :Math.PI/2 - absAng + relAng - k*Math.asin(Math.sin(-relAng)*(targetY  - Math.sqrt(r*r - targetX*targetX)/r));

        State endState = new State(hexPos.getX() + r*Math.cos(angle), hexPos.getY() - r*Math.sin(angle), -(Math.PI / 2 - angle));
        SmartDashboard.putBoolean("inDriveTol", false);

        if(errRadCenter < driveTolerance){
            SmartDashboard.putBoolean("inDriveTol", true);
            endState = new State(startState.getX(),hexPos.getY() - r*Math.sin(angle), startState.getAngle() + 0.01);
        }

        List<State> path = new ArrayList<>();
        path.add(startState);
        path.add(endState);

        SmartDashboard.putString("end", endState.toString());
        System.err.println("end" + endState.toString());

        ProfilingData data = RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.7");

        prof = new Follow2DProfileCommand(path,
                .001, 800,
                data,
                0.7, 1, 1,
                new PIDObject(0.8/data.getMaxLinearVelocity(), 0, 6/data.getMaxLinearAccel()), .01*data.getMaxLinearVelocity(),
                new PIDObject(0.5/data.getMaxAngularVelocity(), 0, 0/data.getMaxAngularAccel()), .01*data.getMaxAngularVelocity(),
                Math.sqrt(Math.pow(difference[0],2) + Math.pow(difference[1],2)) < r);
        cmd = new ThreadedCommand(prof);
        cmd.initialize();
    }

    @Override
    public void execute(){
    }

    @Override
    public void end(boolean interupted) {
        if(!fucked) cmd.end(interupted);
        long time  = System.currentTimeMillis();
        while(System.currentTimeMillis() < time + 500){}
        //new TurnToVision(VisionMaster.Algorithm.HEXAGON,RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularVelocity(),RobotMap.BigRodika.Chassis.MotionData.POWER.get("0.4").getMaxAngularAccel(),0.4).schedule();
    }

    @Override
    public boolean isFinished(){
        return cmd.isFinished();
    }
}
