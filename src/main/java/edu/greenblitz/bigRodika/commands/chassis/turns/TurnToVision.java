package edu.greenblitz.bigRodika.commands.chassis.turns;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.HexAlign;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;

public class TurnToVision extends GBCommand {
    TurnToAngle turn;
    boolean fucked = false;
    private VisionMaster.Algorithm algorithm;
    private double maxV;
    private double maxA;
    private double power;
    private Point posToTurnToByLocalizer;
    private double target;
    private HexAlign hexAlign;

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power) {
        this.algorithm = algorithm;
        this.maxA = maxA;
        this.maxV = maxV;
        this.power = power;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, Point PosToTurnToByLocalizer) {
        this(algorithm, maxV, maxA, power);
        this.posToTurnToByLocalizer = PosToTurnToByLocalizer;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, HexAlign hexAlign) {
        this(algorithm, maxV, maxA, power);
        this.hexAlign = hexAlign;
    }

    @Override
    public void initialize() {
        algorithm.setAsCurrent();

        VisionMaster.getInstance().isLastDataValid();
        double[] diff = VisionMaster.getInstance().getVisionLocation().toDoubleArray();

        if (!VisionMaster.getInstance().isLastDataValid()) {
            if (hexAlign != null) posToTurnToByLocalizer = hexAlign.getHexPos();
            if (posToTurnToByLocalizer == null) {
                fucked = true;
                return;
            }
            diff[0] = posToTurnToByLocalizer.getX() - Chassis.getInstance().getLocation().getX();
            diff[1] = posToTurnToByLocalizer.getY() - Chassis.getInstance().getLocation().getY();
        }
        target = Chassis.getInstance().getAngle() - Math.atan(diff[0] / diff[1]);
//        target = target + RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET;
        turn = new TurnToAngle(Math.toDegrees(target) + Math.toDegrees(RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET), 3, 1
                , maxV, maxA, power, true, 2);
        turn.initialize();
    }

    @Override
    public void execute() {
        if (turn == null) return;
        turn.execute();
    }

    @Override
    public boolean isFinished() {
        if (turn == null) return true;
        return turn.isFinished();
    }

    @Override
    public void end(boolean interupted) {
        SmartDashboard.putNumber("Angle Error =", target - Chassis.getInstance().getAngle());
        if (fucked || turn == null) return;
        turn.end(interupted);
    }
}
