package edu.greenblitz.bigRodika.commands.chassis;

import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;


public class TurnToVision extends GBCommand {
    TurnToAngle turn;
    boolean fuck = false;
    private VisionMaster.Algorithm algorithm;
    private double maxV;
    private double maxA;
    private double power;
    private Double desAngle;
    private Point PosToTurnToByLocalizer;
    private HexAlign hexAlign;

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power){
        this.algorithm  = algorithm;
        this.maxA = maxA;
        this.maxV = maxV;
        this.power = power;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, double desAngle){
        this(algorithm,maxV,maxA,power);
        this.desAngle = desAngle;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, Point PosToTurnToByLocalizer){
        this(algorithm,maxV,maxA,power);
        this.PosToTurnToByLocalizer = PosToTurnToByLocalizer;
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
            if(this.desAngle == null && this.PosToTurnToByLocalizer == null) PosToTurnToByLocalizer = hexAlign.getHexPos();
            diff[0] = PosToTurnToByLocalizer.getX() - Chassis.getInstance().getLocation().getX();
            diff[1] = PosToTurnToByLocalizer.getY() - Chassis.getInstance().getLocation().getY();

            fuck = true;
            return;
        }
        turn = new TurnToAngle(Math.toDegrees(Chassis.getInstance().getAngle() - Math.atan(diff[0]/diff[1])),3,1, maxV, maxA, power);
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
        if(fuck) return;
        turn.end(interupted);
    }
}
