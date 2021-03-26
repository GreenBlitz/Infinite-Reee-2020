package edu.greenblitz.bigRodika;

import edu.greenblitz.gblib.gears.Gear;
import edu.greenblitz.gblib.gears.GearDependentValue;
import org.greenblitz.motion.interpolation.Dataset;
import org.greenblitz.motion.profiling.ProfilingConfiguration;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.HashMap;

public class RobotMap {

    public static class Limbo2 {

        public static class Dome {

            public static final int MOTOR_PORT = 14;
            public static final int POTENTIOMETER_PORT = 3;
            public static final boolean IS_MOTOR_REVERSE = true;
            public static final boolean IS_POTENTIOMETER_REVERSE = true;
            public static final int LIMIT_SWITCH_PORT = 2;

            public static final double ANGLE_PER_POT_VALUE = Math.toRadians(65);
            public static final double DOME_ZERO_ANGLE = Math.toRadians(17);

            public static HashMap<Double, Double> DOME;

            static {
                DOME = new HashMap<>();

                DOME.put(6.3, 0.5);
                DOME.put(4.0, 0.52);

            }

        }

        public static class Turret {
            public static final int MOTOR_PORT = 11;
            public static final int SWITCH_PORT = 3;
            public static final boolean IS_INVERTED = false;
            public static final GearDependentValue<Double> NORMALIZER = new GearDependentValue<>(28672.0,
                    28672.0);//correct 110 present
            public static double ENCODER_VALUE_WHEN_FORWARD = 8974.0;
            public static double ENCODER_90_DEG_ABS = NORMALIZER.getValue() / 4.0;
            public static double ENCODER_VALUE_WHEN_NEGATIVE_90 = ENCODER_VALUE_WHEN_FORWARD -
                    ENCODER_90_DEG_ABS;
            public static double ENCODER_VALUE_WHEN_NEGATIVE_180 = ENCODER_VALUE_WHEN_FORWARD
                    - 2*ENCODER_90_DEG_ABS;

            public static final double TURRET_CAMERA_RADIUS = 0.25;

            public static final int PEAK_CURRENT_LIMIT = 1;
            public static final int PEAK_CURRENT_DURATION = 500;

        }

        public static class Funnel {

            public static class Motors {

                public static final int INSERTER_PORT = 12;
                public static final boolean INSERTER_REVERSED = false;

                public static final int PUSHER_PORT = 15;
                public static final boolean PUSHER_REVERSED = true;

            }

        }

        public static class Intake { // TODO pneumatics here
            public static final int PCM = 21;

            public static class Motors {
                public static final int ROLLER_PORT = 16;
                public static final boolean IS_REVERSED = true;
            }

            public static class Solenoid {
                public static final int FORWARD = 1;
                public static final int REVERSE = 5;
            }
        }

        public static class Shooter {
            public static final int PORT_LEADER = 7;
            public static final int PORT_FOLLOWER = 8;

            public static final boolean IS_INVERTED_LEADER = false;
            public static final boolean IS_INVERTED_FOLLOWER = true;

            public static final double SHOOTER_P = 0.00075;
            public static final double SHOOTER_I = 0.0000005;
            public static final double SHOOTER_D = 0.00006*0.0;

            public static final double SHOOTER_ANGLE_OFFSET = Math.toRadians(0.0);

            public static Dataset distanceToShooterState = new Dataset(3);

            public static final double MINIMUM_SHOOT_DIST = 3.2;
            public static final double MAXIMUM_SHOOT_DIST = 6.7;

            static {
                // First element = rpm. second = dome
                Shooter.distanceToShooterState.addDatapoint(3.2,
                        new double[] {2000, 0.345});
                Shooter.distanceToShooterState.addDatapoint(4.0,
                        new double[] {2100, 0.36});
                Shooter.distanceToShooterState.addDatapoint(5.0,
                        new double[] {2350, 0.403});
                Shooter.distanceToShooterState.addDatapoint(6.1,
                        new double[] {2500, 0.48});
                Shooter.distanceToShooterState.addDatapoint(6.5,
                        new double[] {2500, 0.50});
                Shooter.distanceToShooterState.addDatapoint(6.7,
                        new double[] {2800, 0.486});
            }

        }


        public static class Joystick {
            public static final int MAIN = 0;
            public static final double MAIN_DEADZONE = 0.05;
            public static final int SIDE = 1;
            public static final double SIDE_DEADZONE = 0.05;
        }

        public static class Pneumatics { // TODO: Check compressor shit
            public static final int PCM = 21;

            public static class PressureSensor {
                public static final int PRESSURE = 3;
            }
        }

        public static class Climber { // TODO: check this for now irrelevant
            public static class Motor {
                public static final int HOOK = 9;
                public static final boolean HOOK_REVERSE = true;
                public static final int ELEVATOR = 13;
                public static final GearDependentValue<Double> ELEVATOR_TICKS_PER_METER = new GearDependentValue<>(1.0,1.0);
                public static final GearDependentValue<Double> HOOK_TICKS_PER_METER = new GearDependentValue<>(1.0,1.0);
                public static final double ELEVATOR_RATIO = 0.025;
                public static final boolean ELEVATOR_REVERSE = false;
            }

            public static class Break {
                public static final int SERVO_PORT = 0;
                public static final double RELEASE_VALUE = 1.0;
                public static final double HOLD_VALUE = 0.0;
            }

        }

        public static class Chassis {
            public static final double WHEEL_DIST = 0.622;
            public static final double VISION_CAM_Y_DIST_CENTER = 0.32; // TODO: measure
            public static final double VISION_CAM_X_DIST_CENTER = 0;
            public static final boolean IS_INTAKE_FRONT = false;// Because Alexey is gay

            public static class Motor {
                public static final int
                        LEFT_LEADER = 4,
                        LEFT_FOLLOWER_1 = 5,
                        LEFT_FOLLOWER_2 = 6,
                        RIGHT_LEADER = 1,
                        RIGHT_FOLLOWER_1 = 2,
                        RIGHT_FOLLOWER_2 = 3;


            }

            public static class Shifter { // TODO: check this
                public static final int PCM = 21;

                public static class Solenoid {
                    public static final int FORWARD = 6;
                    public static final int REVERSE = 2;
                }
            }

            public static class Encoder {
                public static final GearDependentValue<Double> // TODO: check this
                        NORM_CONST_SPARK = new GearDependentValue<>(2300.0 * 0.64, 1234.0 / 2.0);
            }

            public static class MotionData { // TODO: calibrate this

                public static final ProfilingConfiguration CONFIG = new ProfilingConfiguration(
                        0.85, 1.0, .0005,
                        0.8, 0.0, 2.0, .01,
                        0.5 * 0, 0, 0, .01, 500);
                public static HashMap<String, ProfilingData> POWER;
                public static HashMap<String, ProfilingData> SPEED;
                public static GearDependentValue<HashMap<String, ProfilingData>> PROF;

                static {

                    POWER = new HashMap<>();
                    SPEED = new HashMap<>();
                    PROF = new GearDependentValue<>(null,null);

                    POWER.put("1.0",
                            new ProfilingData(2.64, 7, 8, 30));
                    POWER.put("0.5",
                            new ProfilingData(1.4, 8.4, 4, 10));

                    // TODO this is dumb
                    POWER.put("0.3",
                            new ProfilingData(1.93*1.2, 4.6, 4.3, 12.6));


                    SPEED.put("0.3",
                            new ProfilingData(1.93*1.2, 4.6, 4.3, 12.6));

                    PROF.setValue(Gear.POWER, POWER);
                    PROF.setValue(Gear.SPEED, SPEED);
                }

            }


        }

        public static final double ANGLE_SHOOTING_TO_INNER_DEG = 18;

    }

    public static class FieldData {
        public static final double HEX_DIST_FROM_INNER = 0.74295;
        public static final double PHYSICAL_ANGLE_LIMIT = 25;

    }
}
