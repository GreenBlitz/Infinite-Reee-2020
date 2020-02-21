package edu.greenblitz.bigRodika.commands.turret;

import edu.greenblitz.bigRodika.utils.VisionMaster;
import org.greenblitz.motion.tolerance.AbsoluteTolerance;
import org.greenblitz.motion.tolerance.ITolerance;

public class TurretByVisionUntilStable extends TurretByVision {

    private static final ITolerance DEFAULT_TOLERANCE = new AbsoluteTolerance(0.5);
    private static final long DEFAULT_TIME_ON_MILLIS = 3000;

    ITolerance tolerance;
    private long millisWantedOnTarget;
    private long timeOnTarget;
    private boolean wasOnTarget;

    public TurretByVisionUntilStable(VisionMaster.Algorithm algorithm, ITolerance tol, long milisOnTarget) {
        super(algorithm);
        tolerance = tol;
        millisWantedOnTarget = milisOnTarget;
    }

    public TurretByVisionUntilStable(VisionMaster.Algorithm algorithm){
        this(algorithm, DEFAULT_TOLERANCE, DEFAULT_TIME_ON_MILLIS);
    }

    public TurretByVisionUntilStable(VisionMaster.Algorithm algorithm, ITolerance tol){
        this(algorithm, tol, DEFAULT_TIME_ON_MILLIS);
    }

    public TurretByVisionUntilStable(VisionMaster.Algorithm algorithm, long millisWantedOnTarget){
        this(algorithm, DEFAULT_TOLERANCE, millisWantedOnTarget);
    }


    @Override
    public boolean isFinished() {
        if(!VisionMaster.getInstance().isLastDataValid()){
            return true;
        }
        if(!tolerance.onTarget(0, VisionMaster.getInstance().getVisionLocation().getRelativeAngle())){
            wasOnTarget = false;
            return false;
        }
        if(!wasOnTarget){
            wasOnTarget = true;
            timeOnTarget = System.currentTimeMillis();
        }
        return System.currentTimeMillis() - timeOnTarget >= millisWantedOnTarget;
    }
}
