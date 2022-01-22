package edu.greenblitz.bigRodika;

import edu.greenblitz.bigRodika.utils.VisionMaster;
import edu.greenblitz.gblib.hid.SmartJoystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import java.util.function.Supplier;

public class OI {
    private static OI instance;

    private SmartJoystick mainJoystick;
    private SmartJoystick secondStick;

    public static final boolean DEBUG = false;

    private OI() {
        mainJoystick = new SmartJoystick(RobotMap.Limbo2.Joystick.MAIN,
                RobotMap.Limbo2.Joystick.MAIN_DEADZONE);
        secondStick = new SmartJoystick(RobotMap.Limbo2.Joystick.SIDE,
                RobotMap.Limbo2.Joystick.SIDE_DEADZONE);

        if (DEBUG) {
            initTestButtons();
        } else {
            initOfficialButtons();
        }
    }


    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    private void initTestButtons() {
//       mainJoystick.A.whileHeld(new TurretByVision(VisionMaster.Algorithm.HEXAGON));
//
      // mainJoystick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

      // mainJoystick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));
//
      // mainJoystick.POV_LEFT.whileHeld(new MoveTurretByConstant(0.2));

      // mainJoystick.POV_RIGHT.whileHeld(new MoveTurretByConstant(-0.2));
//
//    //   secondStick.B.whileHeld(new ParallelCommandGroup(new PushByDifferentConstants(0.6, 0.2), new InsertByConstant(0.6)));
//
//    //   secondStick.Y.whenPressed(new StopShooter());
//
      // mainJoystick.L1.whenPressed(new ToggleExtender());

        Supplier<Double> visionDist = new Supplier<Double>() {
            @Override
            public Double get() {
                return VisionMaster.getInstance().getVisionLocation().getPlaneDistance();
            }
        };

       // mainJoystick.A.whileHeld(new FullShoot(visionDist));
//        mainJoystick.B.whenPressed(new ParallelCommandGroup(
//                new StopShooter(),
//                new ResetDome()
//        ));

    //    mainJoystick.B.whenPressed(new StopShooter());



}

    private void initOfficialButtons() {


        // ---------------------------------------------------------------

//        secondStick.R1.whenPressed(new ShootByConstant(0.4));
//        secondStick.R1.whenReleased(new ParallelCommandGroup(new StopShooter(),
//                new ResetDome(-0.5)));


//        secondStick.X.whenPressed(new ShootByConstant(
//                Shooter.getInstance().getDesiredPower(2000)
//        ));
//        secondStick.X.whenReleased(new GBCommand() {
//            @Override
//            public void initialize() {
//                if (!secondStick.R1.get()) new StopShooter().schedule();
//            }
//
//            @Override
//            public boolean isFinished() {
//                return true;
//            }
//        });

//        secondStick.X.whenPressed(new ThreeStageShoot());

        // TODO: uncomment, microswitch broken so disaling dome function
//        secondStick.POV_UP.whileHeld(new DomeMoveByConstant(0.3));

//        secondStick.POV_DOWN.whileHeld(new DomeMoveByConstant(-0.3));



    }

    public SmartJoystick getMainJoystick() {
        return mainJoystick;
    }

    public SmartJoystick getSideStick() {
        return secondStick;
    }

    protected void setMainJoystick(SmartJoystick mainJoystick) {
        this.mainJoystick = mainJoystick;
    }


}