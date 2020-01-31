package edu.greenblitz.bigRodika.commands.chassis.turns;

import edu.greenblitz.bigRodika.commands.chassis.HexAlign;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.base.Point;

public class TurnToVision extends GBCommand {
    TurnToAngle turn;
    boolean fucked = false;
    private VisionMaster.Algorithm algorithm;
    private double maxV;
    private double maxA;
    private double power;
    private Point posToTurnToByLocalizer;
    private HexAlign hexAlign;

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power){
        this.algorithm  = algorithm;
        this.maxA = maxA;
        this.maxV = maxV;
        this.power = power;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, Point PosToTurnToByLocalizer){
        this(algorithm,maxV,maxA,power);
        this.posToTurnToByLocalizer = PosToTurnToByLocalizer;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, HexAlign hexAlign){
        this(algorithm,maxV,maxA,power);
        this.hexAlign = hexAlign;
    }

    @Override
    public void initialize(){
        algorithm.setAsCurrent();

        VisionMaster.getInstance().isLastDataValid();
        double[] diff = VisionMaster.getInstance().getVisionLocation().toDoubleArray();

        if(!VisionMaster.getInstance().isLastDataValid()) {
            if(hexAlign != null) posToTurnToByLocalizer = hexAlign.getHexPos();
            if(posToTurnToByLocalizer == null){
                fucked = true;
                return;
            }
            diff[0] = posToTurnToByLocalizer.getX() - Chassis.getInstance().getLocation().getX();
            diff[1] = posToTurnToByLocalizer.getY() - Chassis.getInstance().getLocation().getY();
        }
        turn = new TurnToAngle(Math.toDegrees(Chassis.getInstance().getAngle() - Math.atan(diff[0]/diff[1])),3,1, maxV, maxA, power, false, 3);
        turn.initialize();
    }

    @Override
    public void execute(){
        turn.execute();
    }

    @Override
    public boolean isFinished() {
        return turn.isFinished();
    }

    @Override
    public void end(boolean interupted){
        if(fucked) return;
        turn.end(interupted);
    }
}
