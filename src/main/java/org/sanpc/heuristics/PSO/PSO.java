package org.sanpc.heuristics.PSO;

import org.sanpc.Constants;
import org.sanpc.model.Point;
import org.sanpc.model.Route;
import org.sanpc.utils.Distance;

import java.util.*;

public class PSO {
    private final List<Particle> swarm;
    private Route globalBest;
    private double globalBestFitness;

    public PSO(List<Point> operationPoints) {
        this.swarm = new ArrayList<>();
        initialize(operationPoints);
    }

    private void initialize(List<Point> operationPoints) {
        for (int i = 0; i < Constants.N_PARTICLES; i++) {
            List<Point> points = new ArrayList<>(operationPoints);
            Collections.shuffle(points);
            swarm.add(new Particle(new Route(points)));
        }

        globalBest = swarm.getFirst().getPosition();
        globalBestFitness = globalBest.getLength();
        updateGlobalBest();
    }

    public Route optimize() {
        int iteration = 0;
        int stagnationCounter = 0;

        while (iteration < Constants.MAX_ITERATIONS && stagnationCounter < Constants.MAX_STAGNATION) {
            double previousBest = globalBestFitness;

            double inertia = Constants.MIN_INERTIA +
                    (Constants.MAX_INERTIA - Constants.MIN_INERTIA) *
                            (1 - (double) iteration / Constants.MAX_ITERATIONS);

            for (Particle particle : swarm) {
                particle.updateVelocityAndPosition(globalBest, inertia);
            }

            updateGlobalBest();

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

        printOptimizationResults(globalBest);

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

    private void printOptimizationResults(Route bestRoute) {
        System.out.println("\n=== Optimization Results ===");
        System.out.printf("Best route length: %.2f%n", bestRoute.getLength());

        List<Point> points = bestRoute.getPoints();
        System.out.println("\nRoute details:");
        System.out.println("Number of points: " + points.size());

        Point origin = new Point(-1, "X", 0, 0);
        System.out.println("Start: " + origin);

        double totalDistance = 0;
        Point previousPoint = origin;

        for (Point currentPoint : points) {
            double segmentDistance = Distance.euclideanDistance(previousPoint, currentPoint);
            totalDistance += segmentDistance;

            System.out.println(currentPoint);
            previousPoint = currentPoint;
        }

        double finalSegment = Distance.euclideanDistance(previousPoint, origin);
        totalDistance += finalSegment;
        System.out.printf("Return to origin: %s (distance: %.2f)%n", origin, finalSegment);

        System.out.println("\n=== Final Statistics ===");
        System.out.printf("Total distance: %.2f%n", totalDistance);
        System.out.printf("Average segment length: %.2f%n", totalDistance / (points.size() + 1));
    }
}
