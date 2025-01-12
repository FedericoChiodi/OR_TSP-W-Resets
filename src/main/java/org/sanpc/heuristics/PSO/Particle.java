package org.sanpc.heuristics.PSO;

import org.sanpc.Constants;
import org.sanpc.model.Point;

import java.util.*;

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

    /**
     * Genera la velocità iniziale, rappresentata come sequenza di scambi casuali,
     * di una particella con una probabilità direttamente proporzionale al
     * numero totale di punti
     *
     * @return La lista di mosse iniziali generate casualmente
     */
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

    /**
     * Aggiorna la velocità della particella secondo l'equazione di aggiornamento della
     * velocità implementata con approccio probabilistico di inserire o meno le mosse.
     * Aggiorna poi la posizione della particella applicando la velocità e controlla
     * se i personal best sono da aggiornare, e nel caso li aggiorna
     *
     * @param globalBest La route migliore vista dallo swarm
     * @param inertia Il fattore di inerzia della velocità
     */
    public void updateVelocityAndPosition(Route globalBest, double inertia) {
        List<Move> inertiaComponent = velocity.moves();
        List<Move> cognitiveComponent = getMovesTowards(currentPosition, personalBest);
        List<Move> socialComponent = getMovesTowards(currentPosition, globalBest);

        List<Move> newVelocity = new ArrayList<>();

        // Inertia component
        if (Constants.USE_INERTIA) {
            for (Move move : inertiaComponent) {
                if (random.nextDouble() < inertia) {
                    newVelocity.add(move);
                }
            }
        }

        // Cognitive component
        for (Move move : cognitiveComponent) {
            if (random.nextDouble() < Constants.C1 * random.nextDouble()) {
                newVelocity.add(move);
            }
        }

        // Social component
        for (Move move : socialComponent) {
            if (random.nextDouble() < Constants.C2 * random.nextDouble()) {
                newVelocity.add(move);
            }
        }

        velocity = new Velocity(newVelocity);
        currentPosition = velocity.apply(currentPosition);

        // Aggiornamento dei pbest
        currentFitness = currentPosition.getLength();
        if (currentFitness < personalBestFitness) {
            personalBestFitness = currentFitness;
            personalBest = new Route(currentPosition);
        }
    }

    /**
     * Calcola la sequenza di mosse per tramutare una route in un'altra
     *
     * @param from La route di partenza
     * @param to La route a cui si vuole arrivare
     * @return La lista di mosse per tramutare 'from' in 'to'
     */
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

    // Getters
    public Route getPosition() { return currentPosition; }
    public Route getPersonalBest() { return personalBest; }
    public double getPersonalBestFitness() { return personalBestFitness; }
}


