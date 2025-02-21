package org.sanpc.heuristics.LS.tabusearch;

import org.sanpc.Constants;
import org.sanpc.model.Point;
import org.sanpc.model.Route;

import java.util.*;

public class TabuSearch {
    List<Point> operationPoints;
    List<Point> resetPoints;
    Route route;

    public TabuSearch(List<Point> operationPoints, List<Point> resetPoints, List<Point> currentRoute) {
        this.operationPoints = operationPoints;
        this.resetPoints = resetPoints;
        this.route = new Route(currentRoute);
    }

    public Route optimize() {
        System.out.println("Inizio ottimizzazione con Tabu Search...");
        System.out.println("Numero di punti operazione (O): " + operationPoints.size());
        System.out.println("Numero di punti reset (R): " + resetPoints.size());
        System.out.println("Lunghezza iniziale della route: " + evaluate_objective_function(route));
        System.out.println("Penalty per soluzioni non ammissibili: " + Constants.TABU_PENALTY);

        // Step 1: INIT
        int iterations = 0;
        int aspiration_count = 0;
        int tabu_count = 0;
        int non_tabu_count = 0;
        int wentNonAdmissible = 0;
        boolean aspiration;

        Route currentSolution = new Route(route);
        Route bestSolution = new Route(currentSolution);
        Route candidateNextSolution;
        LinkedList<Move> tabuList = new LinkedList<>();

        // Step 2: EXPLORE NEIGHBORHOOD
        while (iterations < Constants.STAGNATION_COUNTER) {
            aspiration = false;
            System.out.println("\n--- Iterazione: " + iterations + " ---");
            System.out.println("Miglior soluzione: " + evaluate_objective_function(bestSolution));
            System.out.println("Soluzione corrente: " + evaluate_objective_function(currentSolution));

            // Neighbourhood exploration
            List<Move> moves = explore_neighborhood(currentSolution);

            for (Move move : moves) {
                candidateNextSolution = new Route(apply_move(currentSolution, move));
                System.out.println("Applicata mossa di tipo: " + move.type + " con indice/i: " + move.index1 + (move.index2 != -1 ? ", " + move.index2 : ""));

                // Is the current route the global best?
                if (evaluate_objective_function(candidateNextSolution) < evaluate_objective_function(bestSolution)) {
                    System.out.println("Trovata nuova soluzione migliore! Valore: " + evaluate_objective_function(candidateNextSolution));
                    bestSolution = new Route(candidateNextSolution);
                    currentSolution = new Route(candidateNextSolution);
                    aspiration_count ++;
                    aspiration = true;
                    break; // Aspiration criteria!
                } else {
                    if (!tabuList.contains(move)) {
                        System.out.println("Mossa non tabù, aggiorno la soluzione corrente.");
                        currentSolution = new Route(candidateNextSolution);
                        move = calculate_inverted_move(move);
                        tabuList.push(move);
                        if (tabuList.size() > Constants.TABU_LIST_SIZE) {
                            System.out.println("Tabu list piena, rimossa la mossa più vecchia.");
                            tabuList.removeLast();
                        }
                        non_tabu_count++;
                        break;
                    } else {
                        tabu_count++;
                        System.out.println("Mossa tabù, ignorata.");
                    }
                }
            }

            if (currentSolution.countViolations() > 0) {
                wentNonAdmissible++;
            }

            // Step 3: STOP OR LOOP
            if (has_solution_improved(currentSolution, bestSolution) || aspiration) {
                if (aspiration) {
                    System.out.println("Soluzione migliorata per aspirazione!, resetto il contatore delle iterazioni.");
                }
                else {
                    System.out.println("Soluzione migliorata, resetto il contatore delle iterazioni.");
                }
                iterations = 0;
            } else {
                System.out.println("Nessun miglioramento, incremento il contatore delle iterazioni.");
                iterations++;
            }
        }

        System.out.println("\nOttimizzazione completata!");
        System.out.println("Miglior valore della funzione obiettivo trovato: " + evaluate_objective_function(bestSolution));
        System.out.println("Numero di violazioni nella migliore soluzione: " + bestSolution.countViolations());
        System.out.println("Numero di aspirazioni: " + aspiration_count);
        System.out.println("Numero di esplorazioni in zone non ammissibili: " + wentNonAdmissible);
        System.out.println("Numero di tabu: " + tabu_count);
        System.out.println("Numero di non tabu: " + non_tabu_count);

        return bestSolution;
    }

    /**
     * Calcola la lunghezza della route, compreso il costo di partenza
     * e arrivo all'origine. Viene aggiunta una penalità per le soluzioni
     * non ammissibili.
     *
     * @param route Route su cui calcolare la funzione obiettivo
     * @return Il valore della funzione obiettivo
     */
    private double evaluate_objective_function(Route route) {
        double length = route.getLength();
        int violations = route.countViolations();
        double penalty = violations * Constants.TABU_PENALTY;
        return length + penalty;
    }

    /**
     * Controlla se la soluzione corrente è migliore della best mai vista
     * valutando la funzione obiettivo.
     *
     * @param current La route corrente
     * @param best La route migliore globalmente
     * @return True se la route corrente è migliore della globale
     */
    private boolean has_solution_improved(Route current, Route best) {
        return evaluate_objective_function(current) < evaluate_objective_function(best);
    }

    /**
     * Calcola l'inverso della mossa passata come parametro.
     *
     * @param move La mossa di cui si vuole ottenere l'inverso
     * @return L'inverso della mossa
     * @throws IllegalStateException Il tipo della mossa non è valido
     */
    private Move calculate_inverted_move(Move move) {
        return switch (move.type) {
            case "ADD" -> new Move(move.index1, move.point, false);
            case "REMOVE" -> new Move(move.index1, move.point, true);
            case "SWAP" -> new Move(move.index2, move.index1);
            default -> throw new IllegalStateException("Unexpected value: " + move.type);
        };
    }

    /**
     * Calcola le migliori TABU_LIST_SIZE+1 mosse da applicare alla route corrente,
     * ordinate per miglioramento (la prima è la migliore).
     * Le mosse ritornate possono essere anche peggiorative
     * rispetto alla soluzione corrente.
     *
     * @param route La route corrente
     * @return Lista delle migliori mosse, ordinate per miglioramento
     */
    private List<Move> explore_neighborhood(Route route) {
        List<MoveWithCost> allMoves = new ArrayList<>();
        Route current = new Route(route);

        // Swap di punti "O"
        for (int i = 0; i < current.getPoints().size(); i++) {
            if (current.getPoints().get(i).getType().equals("O")) {
                for (int j = i + 1; j < current.getPoints().size(); j++) {
                    if (current.getPoints().get(j).getType().equals("O")) {
                        current.swapPoints(i, j);
                        double objAfter = evaluate_objective_function(current);
                        current.swapPoints(i, j);
                        allMoves.add(new MoveWithCost(objAfter, new Move(i, j)));
                    }
                }
            }
        }

        // Rimozione di punti "R"
        for (int i = 0; i < current.getPoints().size(); i++) {
            if (current.getPoints().get(i).getType().equals("R")) {
                Point removedPoint = current.removePointAt(i);
                double objAfter = evaluate_objective_function(current);
                current.addPointAt(i, removedPoint);
                allMoves.add(new MoveWithCost(objAfter, new Move(i, removedPoint, false)));
            }
        }

        // Inserimento di punti "R" non consecutivi
        for (int i = 0; i <= current.getPoints().size(); i++) {
            boolean inserimentoConsentito = i <= 0 || !current.getPoints().get(i - 1).getType().equals("R");
            if (i < current.getPoints().size() && current.getPoints().get(i).getType().equals("R")) {
                inserimentoConsentito = false;
            }
            if (!inserimentoConsentito) {
                continue;
            }

            for (Point rPoint : resetPoints) {
                current.addPointAt(i, rPoint);
                double objAfter = evaluate_objective_function(current);
                current.removePointAt(i);
                allMoves.add(new MoveWithCost(objAfter, new Move(i, rPoint, true)));
            }
        }


        Collections.sort(allMoves);

        List<Move> bestMoves = new ArrayList<>();
        int limit = Math.min(Constants.TABU_LIST_SIZE + 1, allMoves.size());
        for (int i = 0; i < limit; i++) {
            bestMoves.add(allMoves.get(i).move);
        }

        return bestMoves;
    }

    /**
     * Applica la mossa selezionata.
     *
     * @param route La route su cui applicare la mossa
     * @param move La mossa da applicare
     * @return La route dopo l'applicazione della mossa
     */
    private Route apply_move(Route route, Move move) {
        Route newRoute = new Route(route);
        switch (move.type) {
            case "ADD" -> newRoute.addPointAt(move.index1, move.point);
            case "REMOVE" -> newRoute.removePointAt(move.index1);
            case "SWAP" -> newRoute.swapPoints(move.index1, move.index2);
        }
        return newRoute;
    }

    private static class MoveWithCost implements Comparable<MoveWithCost> {
        double cost;
        Move move;

        MoveWithCost(double cost, Move move) {
            this.cost = cost;
            this.move = move;
        }

        @Override
        public int compareTo(MoveWithCost other) {
            return Double.compare(this.cost, other.cost);
        }
    }

}