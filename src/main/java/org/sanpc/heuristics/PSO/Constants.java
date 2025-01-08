package org.sanpc.heuristics.PSO;

public class Constants {
    // TSP related constants
    public static final int LENGTH = 50;
    public static final int WIDTH = 50;
    public static final int N_OPERATIONS = 10;

    // PSO parameters
    public static final int N_PARTICLES = 50;
    public static final double C1 = 2;
    public static final double C2 = 2;
    public static final double MAX_INERTIA = 0.9;
    public static final double MIN_INERTIA = 0.4;
    public static final boolean USE_INERTIA = false;

    public static final int MAX_ITERATIONS = 10000;
    public static final int MAX_STAGNATION = 100;

    public static final double INITIAL_VELOCITY_FACTOR = 0.2;
    public static final boolean USE_2_OPT = false; // Enable local search
}
