package edu.greenblitz.bigRodika.commands.chassis.turns;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.chassis.motion.HexAlign;
import edu.greenblitz.bigRodika.commands.turret.TurretByVision;
import edu.greenblitz.bigRodika.subsystems.Chassis;
import edu.greenblitz.bigRodika.subsystems.Turret;
import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.base.Point;

public class TurnToVision extends GBCommand {
    GBCommand turn;
    boolean fucked = false;
    private VisionMaster.Algorithm algorithm;
    private double maxV;
    private double maxA;
    private double power;
    private Point posToTurnToByLocalizer;
    private double target;
    private HexAlign hexAlign;
    private boolean useTurret;

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, boolean useTurret) {
        if (useTurret) {
            require(Turret.getInstance());
        }
        this.algorithm = algorithm;
        this.maxA = maxA;
        this.maxV = maxV;
        this.power = power;
        this.useTurret = useTurret;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, boolean useTurret, Point PosToTurnToByLocalizer) {
        this(algorithm, maxV, maxA, power, useTurret);
        this.posToTurnToByLocalizer = PosToTurnToByLocalizer;
    }

    public TurnToVision(VisionMaster.Algorithm algorithm, double maxV, double maxA,
                        double power, boolean useTurret, HexAlign hexAlign) {
        this(algorithm, maxV, maxA, power, useTurret);
        this.hexAlign = hexAlign;
    }

    @Override
    public void initialize() {
        algorithm.setAsCurrent();

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

        if (useTurret) {
            turn =
                    new TurretByVision(algorithm);
        } else {
            target = Chassis.getInstance().getAngle() - Math.atan(diff[0] / diff[1]);
            turn = new TurnToAngle(Math.toDegrees(target) + Math.toDegrees(RobotMap.Limbo2.Shooter.SHOOTER_ANGLE_OFFSET), 3, 1
                    , maxV, maxA, power, true, 2);
        }
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
    public void end(boolean interrupted) {
        SmartDashboard.putNumber("Angle Error =", target - Chassis.getInstance().getAngle());
        if (fucked || turn == null) return;
        turn.end(interrupted);
    }
}
