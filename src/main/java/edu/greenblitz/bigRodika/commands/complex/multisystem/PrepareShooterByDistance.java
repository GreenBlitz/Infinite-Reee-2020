package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.Robot;
import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.subsystems.Dome;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import org.greenblitz.motion.base.TwoTuple;

import java.util.function.Supplier;

import static edu.greenblitz.bigRodika.RobotMap.Limbo2.Shooter.DISTANCE_ERROR_THRESHOLD;

public class PrepareShooterByDistance extends GBCommand {

    private Supplier<Double> distanceSupplier;
    private DomeApproachSwiftly domeCommand;
    private FullyAutoThreeStage shooterCommand;

    public PrepareShooterByDistance(Supplier<Double> distanceSupplier) {

        this.distanceSupplier = distanceSupplier;
        require(Dome.getInstance());
        require(Shooter.getInstance());

    }

    public void testing(){
        System.out.println("sup to be true");
        testForTheFunctionInitialize(6.1);
        System.out.println("sup to be false");
        testForTheFunctionInitialize(2.0);
        System.out.println("sup to be true");
        testForTheFunctionInitialize(3.19);
        System.out.println("sup to be true");
        testForTheFunctionInitialize(6.67);
        System.out.println("sup to be false");
        testForTheFunctionInitialize(4.0);
    }

    public void testForTheFunctionInitialize(double distance) {
        double idealX1 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getFirst().getFirst();
        double idealX2 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getSecond().getFirst();
        double minDiff = Math.min(Math.abs(idealX1 - distance), Math.abs(idealX2 - distance));
        boolean shoot;

        if (minDiff <= DISTANCE_ERROR_THRESHOLD) {
            shoot = true;
        } else {
            shoot = false;
        }
        if (shoot) {
            System.out.println("Shooted");

        } else {
            System.out.println("Did not shoot");
        }
    }


    @Override
    public void initialize() {
        double distance = distanceSupplier.get();
        double idealX1 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getFirst().getFirst();
        double idealX2 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getSecond().getFirst();
        double minDiff = Math.min(Math.abs(idealX1 - distance), Math.abs(idealX2 - distance));
        boolean shoot;

        if (minDiff <= DISTANCE_ERROR_THRESHOLD) {
            shoot = true;
        } else {
            shoot = false;
        }
        if (shoot) {
            System.out.println("Shooted");
            double[] shooterState = RobotMap.Limbo2.Shooter.
                    distanceToShooterState.linearlyInterpolate(
                    distance
            );

            domeCommand = new DomeApproachSwiftly(shooterState[1]);
            shooterCommand = new FullyAutoThreeStage(shooterState[0]);

            domeCommand.initialize();
            shooterCommand.initialize();
        } else {
            System.out.println("Did not shoot");
            end(false);
        }
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
