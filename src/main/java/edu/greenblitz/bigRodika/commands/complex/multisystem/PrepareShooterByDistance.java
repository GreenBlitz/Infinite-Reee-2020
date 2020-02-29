package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.subsystems.Dome;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import java.util.function.Supplier;

public class PrepareShooterByDistance extends GBCommand {

    private Supplier<Double> distanceSupplier;
    private DomeApproachSwiftly domeCommand;
    private FullyAutoThreeStage shooterCommand;

    public PrepareShooterByDistance(Supplier<Double> distanceSupplier){

        this.distanceSupplier = distanceSupplier;
        require(Dome.getInstance());
        require(Shooter.getInstance());

    }

    @Override
    public void initialize() {

        double[] shooterState = RobotMap.Limbo2.Shooter.
                distanceToShooterState.linearlyInterpolate(
                        distanceSupplier.get()
        );
        domeCommand = new DomeApproachSwiftly(shooterState[1]);
        shooterCommand = new FullyAutoThreeStage(shooterState[0]);

        domeCommand.initialize();
        shooterCommand.initialize();
    }

    @Override
    public void execute() {
        domeCommand.execute();
        shooterCommand.execute();
    }

    @Override
    public void end(boolean interrupted) {
        domeCommand.end(interrupted);
        shooterCommand.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return domeCommand.isFinished() && shooterCommand.isFinished();
    }
}
