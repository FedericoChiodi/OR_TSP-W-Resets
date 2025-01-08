package org.sanpc.heuristics.PSO;

import org.sanpc.model.Point;
import org.sanpc.utils.Distance;

import java.util.*;

public class PSO {
    private final List<Particle> swarm;
    private Route globalBest;
    private double globalBestFitness;

    public PSO() {
        this.swarm = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        List<Point> basePoints = generatePoints();

        // Initialize particles with different random permutations
        for (int i = 0; i < Constants.N_PARTICLES; i++) {
            List<Point> points = new ArrayList<>(basePoints);
            Collections.shuffle(points);
            swarm.add(new Particle(new Route(points)));
        }

        // Initialize global best
        globalBest = swarm.getFirst().getPosition();
        globalBestFitness = globalBest.getLength();
        updateGlobalBest();
    }

    private List<Point> generatePoints() {
        List<Point> points = new ArrayList<>();
        Set<String> coordinates = new HashSet<>();
        Random random = new Random();

        for (int i = 0; i < Constants.N_OPERATIONS; i++) {
            int x, y;
            String coord;
            do {
                x = random.nextInt(Constants.LENGTH);
                y = random.nextInt(Constants.WIDTH);
                coord = x + "," + y;
            } while (!coordinates.add(coord));

            points.add(new Point(i, "O", x, y));
        }

        return points;
    }

    public Route optimize() {
        int iteration = 0;
        int stagnationCounter = 0;

        while (iteration < Constants.MAX_ITERATIONS && stagnationCounter < Constants.MAX_STAGNATION) {
            double previousBest = globalBestFitness;

            double inertia = Constants.MAX_INERTIA - (Constants.MAX_INERTIA - Constants.MIN_INERTIA) * 2 * ((double) iteration / Constants.MAX_ITERATIONS);

            // Update all particles
            for (Particle particle : swarm) {
                particle.updateVelocityAndPosition(globalBest, inertia);
            }

            // Update global best
            updateGlobalBest();

            // Check for stagnation
            if (Math.abs(previousBest - globalBestFitness) < 1e-6) {
                stagnationCounter++;
            } else {
                stagnationCounter = 0;
            }

            System.out.printf("Iteration %d: Best fitness = %.2f Inertia: %.2f%n", iteration, globalBestFitness, inertia);

            iteration++;
        }

        System.out.printf("Optimization completed after %d iterations%n", iteration);
        System.out.printf("Final best fitness: %.2f%n", globalBestFitness);

        return globalBest;
    }

    private void updateGlobalBest() {
        for (Particle particle : swarm) {
            if (particle.getPersonalBestFitness() < globalBestFitness) {
                globalBestFitness = particle.getPersonalBestFitness();
                globalBest = new Route(particle.getPersonalBest());
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Inizializza ed esegui PSO
            System.out.println("Starting PSO optimization for TSP...");
            long startTime = System.currentTimeMillis();

            PSO pso = new PSO();
            Route bestRoute = pso.optimize();

            long endTime = System.currentTimeMillis();
            double executionTime = (endTime - startTime) / 1000.0;

            // Stampa risultati dettagliati
            System.out.println("\n=== Optimization Results ===");
            System.out.printf("Execution time: %.2f seconds%n", executionTime);
            System.out.printf("Best route length: %.2f%n", bestRoute.getLength());

            // Stampa statistiche della route
            List<Point> points = bestRoute.getPoints();
            System.out.println("\nRoute details:");
            System.out.println("Number of points: " + points.size());

            // Stampa la sequenza dei punti
            System.out.println("\nRoute sequence:");
            Point origin = new Point(-1, "X", 0, 0);
            System.out.println("Start: " + origin);

            double totalDistance = 0;
            Point previousPoint = origin;

            for (Point currentPoint : points) {
                double segmentDistance = Distance.euclideanDistance(previousPoint, currentPoint);
                totalDistance += segmentDistance;

                // System.out.printf("Point %d: %s (distance from previous: %.2f)%n", i + 1, currentPoint, segmentDistance);
                System.out.println(currentPoint);

                previousPoint = currentPoint;
            }

            // Aggiungi distanza finale per tornare all'origine
            double finalSegment = Distance.euclideanDistance(previousPoint, origin);
            totalDistance += finalSegment;
            System.out.printf("Return to origin: %s (distance: %.2f)%n",
                    origin, finalSegment);

            // Stampa statistiche finali
            System.out.println("\n=== Final Statistics ===");
            System.out.printf("Total distance: %.2f%n", totalDistance);
            System.out.printf("Average segment length: %.2f%n",
                    totalDistance / (points.size() + 1));

        } catch (Exception e) {
            System.err.println("Error during PSO execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
