package edu.greenblitz.bigRodika.commands.complex.autonomous;

import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.Command;

public class PathByVision extends GBCommand {

    private Command path1;
    private Command path2;
    private double d1;
    private double d2;

    public PathByVision(Command path1, Command path2, double d1, double d2) {
        this.path1 = path1;
        this.path2 = path2;
        this.d1 = d1;
        this.d2 = d2;
    }

    @Override
    public void initialize() {
        double distance = VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
        if (Math.abs(distance - d1) < Math.abs(distance - d2)) {
            path1.schedule();//runs first command
        } else {
            path2.schedule();//runs second command
        }
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
