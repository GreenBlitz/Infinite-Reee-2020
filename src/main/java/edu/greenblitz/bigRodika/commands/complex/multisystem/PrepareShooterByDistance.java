package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.subsystems.Dome;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Arrays;
import java.util.function.Supplier;

public class PrepareShooterByDistance extends GBCommand {

    private Supplier<Double> distanceSupplier;
    private DomeApproachSwiftly domeCommand;
    private FullyAutoThreeStage shooterCommand;
    // temp for debugging
    private double[] shooterState;

    public PrepareShooterByDistance(Supplier<Double> distanceSupplier){

        this.distanceSupplier = distanceSupplier;
        require(Dome.getInstance());
        require(Shooter.getInstance());

    }

    @Override
    public void initialize() {

        System.out.println(distanceSupplier.get());

        shooterState = RobotMap.Limbo2.Shooter.
                distanceToShooterState.getAdjesent(
                        distanceSupplier.get()
        ).getFirst().getSecond();

        SmartDashboard.putString("Shooter", Arrays.toString(shooterState));
        domeCommand = new DomeApproachSwiftly(shooterState[1]);
        shooterCommand = new FullyAutoThreeStage(shooterState[0]);

        domeCommand.initialize();
        shooterCommand.initialize();
    }

    @Override
    public void execute() {

        SmartDashboard.putNumber("Shooter Speed Error", Shooter.getInstance().getAbsoluteShooterSpeed() - shooterState[0]);
        SmartDashboard.putNumber("Dome Error", Dome.getInstance().getPotentiometerValue() - shooterState[1]);

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
