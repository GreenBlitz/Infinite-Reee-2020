package edu.greenblitz.bigRodika;

import edu.greenblitz.gblib.gears.GearDependentValue;
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
            public static final int LIMIT_SWITCH_PORT = 0;

            public static HashMap<String, Double> DOME;

            static{
                DOME = new HashMap<>();

                DOME.put("3", 0.1);//TODO: CHECK THIS FOR REAL
                DOME.put("4", 0.2);//TODO: CHECK THIS FOR REAL
                DOME.put("5", 0.3);//TODO: CHECK THIS FOR REAL


            }

        }

        public static class Turret {
            public static final int MOTOR_PORT = 11;
            public static final int SWITCH_PORT = 0; // TODO: everything after this including
            public static final boolean IS_INVERTED = false;
            public static final GearDependentValue<Double> NORMALIZER = new GearDependentValue<>(1.0,
                    1.0);

            public static final double TURRET_CAMERA_RADIUS = 0.23;
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
            public static final int PCM = 22;

            public static class Motors {
                public static final int ROLLER_PORT = 16;
                public static final boolean IS_REVERSED = true;
            }

            public static class Solenoid {
                public static final int FORWARD = 5;
                public static final int REVERSE = 7;
            }
        }

        public static class Shooter {
            public static final int PORT_LEADER = 7;
            public static final int PORT_FOLLOWER = 8;

            public static final boolean IS_INVERTED_LEADER = false;
            public static final boolean IS_INVERTED_FOLLOWER = true;

            public static final double SHOOTER_P = 0.005;
            public static final double SHOOTER_I = 0.00002;
            public static final double SHOOTER_D = 0.3;

            public static final double SHOOTER_ANGLE_OFFSET = Math.toRadians(0.0);

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
                public static final boolean HOOK_REVERSE = false;
                public static final int ELEVATOR = 10;
                public static final boolean ELEVATOR_REVERSE = false;
            }

            public static class Break {
                public static final int FORWARD = 3;
                public static final int REVERSE = 4;
                public static final int PCM = 21;
            }

        }

        public static class Chassis {
            public static final double WHEEL_DIST = 0.622;
            public static final double VISION_CAM_Y_DIST_CENTER = 0.32; // TODO: measure
            public static final double VISION_CAM_X_DIST_CENTER = 0;
            public static final boolean IS_INTAKE_FRONT = true;// Because Alexey is gay

            public static class Motor {
                public static final int
                        LEFT_LEADER = 1,
                        LEFT_FOLLOWER_1 = 2,
                        LEFT_FOLLOWER_2 = 3,
                        RIGHT_LEADER = 4,
                        RIGHT_FOLLOWER_1 = 5,
                        RIGHT_FOLLOWER_2 = 6;


            }

            public static class Shifter { // TODO: check this
                public static final int PCM = 21;

                public static class Solenoid {
                    public static final int FORWARD = 3;
                    public static final int REVERSE = 6;
                }
            }

            public static class Encoder {
                public static final GearDependentValue<Double> // TODO: check this
                        NORM_CONST_SPARK = new GearDependentValue<>(2300.0, 1234.0 / 2.0);
            }

            public static class MotionData { // TODO: calibrate this

                public static final ProfilingConfiguration CONFIG = new ProfilingConfiguration(
                        1.2, 1.0, .0005,
                        0.6, .002, 8, .01,
                        0.5, 0, 12, .01, 500);
                public static HashMap<String, ProfilingData> POWER;
                public static HashMap<String, ProfilingData> SPEED;

                static {

                    POWER = new HashMap<>();

                    POWER.put("0.4", new ProfilingData(0.7, 4.6, 2.1, 10));
                    POWER.put("0.7", new ProfilingData(1.15, 10, 4, 15));

//                    POWER.put("0.5", new ProfilingData(1.5*0.9, 4, 2.6, 8));
                    POWER.put("0.5", new ProfilingData(1.6, 6, 3, 12.5));

                }

            }


        }

        public static final double ANGLE_SHOOTING_TO_INNER_DEG = 18;

    }

    public static class FieldData{
        public static final double HEX_DIST_FROM_INNER = 0.74295;
        public static final double PHYSICAL_ANGLE_LIMIT = 25;

    }
}
