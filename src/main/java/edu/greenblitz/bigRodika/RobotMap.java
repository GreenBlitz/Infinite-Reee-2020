package edu.greenblitz.bigRodika;

import edu.greenblitz.gblib.gears.GearDependentValue;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.HashMap;

public class RobotMap {

    public static class Limbo2 {

        public static class Dome {

            public static final int MOTOR_PORT = 0;
            public static final int POTENTIOMETER_PORT = 0;
            public static final boolean IS_MOTOR_REVERS = false;
            public static final boolean IS_POTENTIOMETER_REVERSE = false;

        }

        public static class Funnel {

            public static class Encoder {
                // This must be the same for power and speed cause they have no meaning here.
                public static final GearDependentValue<Double> NORMALIZER = new GearDependentValue<>(1.0,
                        1.0);
            }

            public static class Motors {

                public static final int INSERTER_PORT = 10;
                public static final int PUSHER_PORT = 12;

            }

        }

        public static class Intake {
            public static class Motors {
                public static final int ROLLER_PORT = 13;
                public static final boolean IS_REVERSED = false;
            }

            public static class Solenoid {
                public static final int FORWARD = 5;
                public static final int REVERSE = 7;
            }

            public static final int PCM = 22;
        }

        public static class Shooter {
            public static final int PORT = 6;
            // This must be the same for power and speed cause they have no meaning here.
            public static final GearDependentValue<Double> NORMALIZER = new GearDependentValue<>(1.0,
                    1.0);
            public static final boolean IS_INVERTED = true;
            public static final double SHOOTER_ANGLE_OFFSET = Math.toRadians(-8.0);

        }


        public static class Joystick {
            public static final int MAIN = 0;
        }

        public static class Pneumatics {
            public static final int PCM = 21;

            public static class PressureSensor {
                public static final int PRESSURE = 3;
            }
        }

        public static class Chassis {
            public static class Motor {
                public static final int LEFT_VICTOR = 2, // 2
                        RIGHT_VICTOR = 3, // 3
                        LEFT_TALON = 1, // 1
                        RIGHT_TALON = 4; // 4
                public static final int RIGHT_LEADER = 4,
                        RIGHT_FOLLOWER_1 = 5,
                        RIGHT_FOLLOWER_2 = 6,
                        LEFT_LEADER = 1,
                        LEFT_FOLLOWER_1 = 2,
                        LEFT_FOLLOWER_2 = 3;


            }

            public static class Shifter {
                public static class Solenoid {
                    public static final int FORWARD = 3;
                    public static final int REVERSE = 6;
                }

                public static final int PCM = 21;
            }

            public static class Encoder {
                public static final int LEFT_PORT_A = 2,
                        LEFT_PORT_B = 3,
                        RIGHT_PORT_A = 0,
                        RIGHT_PORT_B = 1;

                public static final GearDependentValue<Double>
                        NORM_CONST_SPARK = new GearDependentValue<>(2300.0, 1234.0 / 2.0),
                        NORM_CONST_LEFT = new GearDependentValue<>(2100.0, 2100.0),
                        NORM_CONST_RIGHT = new GearDependentValue<>(1721.0, 1721.0);
            }

            public static final double WHEEL_DIST = 0.57;

            public static final double VISION_CAM_Y_DIST_CENTER = 0.32;

            public static final double VISION_CAM_X_DIST_CENTER = 0.03;

            public static final double SHOOTER_X_DIST_CENTER = 0.0;

            public static final double SHOOTER_Y_DIST_CENTER = 0.0;

            public static class MotionData {

                public static final double HEXAGON_CAMERA_H_DIFF = 1.4;
                public static HashMap<String, ProfilingData> POWER;
                public static HashMap<String, ProfilingData> SPEED;

                static {

                    POWER = new HashMap<>();

                    POWER.put("0.4", new ProfilingData(0.7, 4.6, 2.1, 10));
                    POWER.put("0.7", new ProfilingData(1.15, 10, 4, 15));

//                    POWER.put("0.5", new ProfilingData(1.5*0.9, 4, 2.6, 8));
                    POWER.put("0.5", new ProfilingData(1.6, 6, 3, 12.5));

                    // 3.6

                }

            }


        }

        public static class PDPPorts{
            public static final int SHOOTER = 13;
        }
    }
}
