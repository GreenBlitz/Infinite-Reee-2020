package edu.greenblitz.bigRodika.commands.complex.multisystem;

import edu.greenblitz.bigRodika.RobotMap;
import edu.greenblitz.bigRodika.commands.dome.DomeApproachSwiftly;
import edu.greenblitz.bigRodika.commands.shooter.StopShooter;
import edu.greenblitz.bigRodika.commands.shooter.pidshooter.threestage.FullyAutoThreeStage;
import edu.greenblitz.bigRodika.subsystems.Dome;
import edu.greenblitz.bigRodika.subsystems.Shooter;
import edu.greenblitz.gblib.command.GBCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.Supplier;

import static edu.greenblitz.bigRodika.RobotMap.Limbo2.Shooter.DISTANCE_ERROR_THRESHOLD;

public class PrepareShooterByDistance extends GBCommand {

    private boolean interrupt = false;
    private Supplier<Double> distanceSupplier;
    private DomeApproachSwiftly domeCommand;
    private FullyAutoThreeStage shooterCommand;

    public PrepareShooterByDistance(Supplier<Double> distanceSupplier) {

        this.distanceSupplier = distanceSupplier;
        require(Dome.getInstance());
        require(Shooter.getInstance());

    }

    public void testing() {
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
        double idealX1, idealX2;

        try {
            idealX1 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getFirst().getFirst();
            idealX2 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getSecond().getFirst();
        } catch (Exception e){
            e.printStackTrace();
            this.interrupt = true;
            return;
        }
        double minDiff = Math.min(Math.abs(idealX1 - distance), Math.abs(idealX2 - distance));
        boolean shoot;

        if (minDiff <= DISTANCE_ERROR_THRESHOLD) {
            shoot = true;
        } else {
            shoot = false;
        }
        if (shoot) {
            System.out.println("Shot");
        } else {
            System.out.println("Did not shoot");
        }
    }


    @Override
    public void initialize() {
        double distance = distanceSupplier.get();
        SmartDashboard.putNumber("Plane Distance From Outer Port", distance);

        double[] shooterState;

        try {
            shooterState = RobotMap.Limbo2.Shooter.
                    distanceToShooterState.linearlyInterpolate(
                    distance
            );
        } catch(Exception e) {
            e.printStackTrace();
            this.interrupt = true;
            return;
        }

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
        SmartDashboard.putString("finished", "yes");
        domeCommand.end(interrupted);
        shooterCommand.end(interrupted);
        new StopShooter().schedule();
    }

    @Override
    public boolean isFinished() {
        double distance = distanceSupplier.get();
        double idealX1 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getFirst().getFirst();
        double idealX2 = RobotMap.Limbo2.Shooter.distanceToShooterState.getAdjesent(distance).getSecond().getFirst();
        double minDiff = Math.min(Math.abs(idealX1 - distance), Math.abs(idealX2 - distance));
        SmartDashboard.putBoolean("Shoot", minDiff <= DISTANCE_ERROR_THRESHOLD);

        return (domeCommand.isFinished() && shooterCommand.isFinished()) || minDiff >= DISTANCE_ERROR_THRESHOLD || interrupt;
    }
}