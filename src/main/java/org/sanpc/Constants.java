package org.sanpc;

public class Constants {
    // TSP related constants
    public static final int LENGTH = 25;
    public static final int WIDTH = 25;
    public static final int K = 4;
    public static final int N_OPERATIONS = 15;
    public static final int N_RESETS = 5;

    // PSO parameters
    public static final int N_PARTICLES = 500;
    public static final double C1 = 2.05;
    public static final double C2 = 1.5;
    public static final double MAX_INERTIA = 0.9;
    public static final double MIN_INERTIA = 0.4;
    public static final boolean USE_INERTIA = true;
    public static final int MAX_ITERATIONS = 100000;
    public static final int MAX_STAGNATION = 200;
    public static final double INITIAL_VELOCITY_FACTOR = 1;


    public static final boolean USE_2_OPT = false;
}
