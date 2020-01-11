package edu.greenblitz.bigRodika;

public class RobotMap {

    public static class BigRodika {
        public static class Joystick {
            public static final int MAIN = 0;
        }

        public static class Chassis {
            public static class Motor {
                public static final int LEFT_VICTOR = 2,
                                        RIGHT_VICTOR = 3,
                                        LEFT_TALON = 1,
                                        RIGHT_TALON = 4;
            }

            public static class Encoder{
                public static final int LEFT_PORT_A = 2,
                                        LEFT_PORT_B = 3,
                                        RIGHT_PORT_A = 0,
                                        RIGHT_PORT_B = 1;

                public static final double NORM_CONST_LEFT = 1.0/(1.2/2500.0),
                                           NORM_CONST_RIGHT = 1.0/(1.2/2100.0);
            }

            public static final double WHEEL_DIST = 0.57;

            public static final double MAX_ANG_V = 0.034,
                                       MAX_ANG_A = 0.135;

        }

    }
}
