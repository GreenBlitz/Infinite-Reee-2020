package edu.greenblitz.bigRodika;

import edu.greenblitz.gblib.Tuple;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.greenblitz.motion.profiling.ProfilingData;

import java.util.HashMap;

public class RobotMap {

    public static class BigRodika {
        public static class Joystick {
            public static final int MAIN = 0;
        }

        public static class Chassis {
            public static class Motor {
                /*public static final int LEFT_VICTOR = 2,
                                        RIGHT_VICTOR = 3,
                                        LEFT_TALON = 1,
                                        RIGHT_TALON = 4; */
                public static final int  RIGHT_LEADER = 4,
                RIGHT_FOLLOWER_1 = 5,
                RIGHT_FOLLOWER_2 = 6,
                LEFT_LEADER = 1,
                LEFT_FOLLOWER_1 = 2,
                LEFT_FOLLOWER_2 = 3;


            }

            public static class Encoder{
                public static final int LEFT_PORT_A = 2,
                                        LEFT_PORT_B = 3,
                                        RIGHT_PORT_A = 0,
                                        RIGHT_PORT_B = 1;

                public static final double NORM_CONST_LEFT = 1.0/(1.2/2500.0),
                                           NORM_CONST_RIGHT = 1.0/(1.2/2100.0),
                                           MORM_CONST_SPARK = 2300;
            }

            public static class MotionData {

                public static HashMap<String, ProfilingData> POWER;
                public static HashMap<String, ProfilingData> SPEED;


                static {

                    POWER = new HashMap<>();
                    POWER.put("0.4", new ProfilingData(0.7,4.6,2.1,10));
                    POWER.put("0.7", new ProfilingData(1.15, 3.75, 4, 10));
                    SmartDashboard.putBoolean("Setup motion vals", true);

                }

            }

            public static final double WHEEL_DIST = 0.595;

        }



    }
}
