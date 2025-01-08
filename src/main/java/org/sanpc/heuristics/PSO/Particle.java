package org.sanpc.heuristics.PSO;

import org.sanpc.model.Point;

import java.util.*;
import org.sanpc.utils.Distance;

public class Particle {
    private Route currentPosition;
    private Route personalBest;
    private Velocity velocity;
    private double currentFitness;
    private double personalBestFitness;
    private final Random random;

    public Particle(Route initialPosition) {
        this.random = new Random();
        this.currentPosition = initialPosition;
        this.currentFitness = initialPosition.getLength();
        this.personalBest = new Route(initialPosition);
        this.personalBestFitness = currentFitness;
        this.velocity = new Velocity(generateInitialVelocity());
    }

    private List<Move> generateInitialVelocity() {
        int numMoves = (int) (Constants.N_OPERATIONS * Constants.INITIAL_VELOCITY_FACTOR);
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < numMoves; i++) {
            int idx1 = random.nextInt(Constants.N_OPERATIONS);
            int idx2 = random.nextInt(Constants.N_OPERATIONS);
            if (idx1 != idx2) {
                moves.add(new Move(idx1, idx2));
            }
        }
        return moves;
    }

    public void updateVelocityAndPosition(Route globalBest, double inertia) {
        // Generate velocity components
        List<Move> inertiaComponent = velocity.moves();
        List<Move> cognitiveComponent = getMovesTowards(currentPosition, personalBest);
        List<Move> socialComponent = getMovesTowards(currentPosition, globalBest);

        // Apply constriction factor and combine components
        List<Move> newVelocity = new ArrayList<>();

        if (Constants.USE_INERTIA) {
            for (Move move : inertiaComponent) {
                if (random.nextDouble() < inertia) {
                    newVelocity.add(move);
                }
            }
        }

        // Add cognitive component
        for (Move move : cognitiveComponent) {
            if (random.nextDouble() < Constants.C1 * random.nextDouble()) {
                newVelocity.add(move);
            }
        }

        // Add social component
        for (Move move : socialComponent) {
            if (random.nextDouble() < Constants.C2 * random.nextDouble()) {
                newVelocity.add(move);
            }
        }

        // Update velocity and position
        velocity = new Velocity(newVelocity);
        currentPosition = velocity.apply(currentPosition);

        // Apply local search if enabled
        if (Constants.USE_2_OPT) {
            apply2OptImprovement();
        }

        // Update fitness and personal best
        currentFitness = currentPosition.getLength();
        if (currentFitness < personalBestFitness) {
            personalBestFitness = currentFitness;
            personalBest = new Route(currentPosition);
        }
    }

    private List<Move> getMovesTowards(Route from, Route to) {
        List<Move> moves = new ArrayList<>();
        List<Point> fromPoints = new ArrayList<>(from.getPoints());
        List<Point> toPoints = new ArrayList<>(to.getPoints());

        for (int i = 0; i < fromPoints.size(); i++) {
            Point currentPoint = fromPoints.get(i);
            Point targetPoint = toPoints.get(i);

            if (!currentPoint.equals(targetPoint)) {
                int swapIdx = -1;
                for (int j = i + 1; j < fromPoints.size(); j++) {
                    if (fromPoints.get(j).equals(targetPoint)) {
                        swapIdx = j;
                        break;
                    }
                }
                if (swapIdx != -1) {
                    moves.add(new Move(i, swapIdx));
                    Collections.swap(fromPoints, i, swapIdx);
                }
            }
        }
        return moves;
    }

    private void apply2OptImprovement() {
        boolean improved;
        do {
            improved = false;
            List<Point> points = currentPosition.getPoints();
            double bestGain = 0;
            int bestI = -1;
            int bestJ = -1;

            // 2-opt standard solo tra i punti operazione
            for (int i = 0; i < points.size() - 1; i++) {
                for (int j = i + 2; j < points.size(); j++) {
                    double gain = calculate2OptGain(points, i, j);
                    if (gain > bestGain) {
                        bestGain = gain;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }

            if (bestGain > 0) {
                improved = true;
                perform2OptSwap(points, bestI, bestJ);
                currentPosition = new Route(points);
            }
        } while (improved);
    }

    private double calculate2OptGain(List<Point> points, int i, int j) {
        Point p1 = points.get(i);
        Point p2 = points.get(i + 1);
        Point p3 = points.get(j);
        Point p4 = points.get((j + 1) % points.size());

        double currentDistance = Distance.euclideanDistance(p1, p2) + Distance.euclideanDistance(p3, p4);
        double newDistance = Distance.euclideanDistance(p1, p3) + Distance.euclideanDistance(p2, p4);

        return currentDistance - newDistance;
    }

    private void perform2OptSwap(List<Point> points, int i, int j) {
        int left = i + 1;
        int right = j;
        while (left < right) {
            Collections.swap(points, left, right);
            left++;
            right--;
        }
    }

    // Getters
    public Route getPosition() { return currentPosition; }
    public Route getPersonalBest() { return personalBest; }
    public double getPersonalBestFitness() { return personalBestFitness; }
}


