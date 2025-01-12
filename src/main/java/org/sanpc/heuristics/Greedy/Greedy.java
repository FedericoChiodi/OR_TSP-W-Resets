package org.sanpc.heuristics.Greedy;

import org.sanpc.Constants;
import org.sanpc.model.Point;
import org.sanpc.utils.Distance;

import java.util.ArrayList;
import java.util.List;

public class Greedy {

    /**
     * L'algoritmo inizia dall'origine, seleziona ripetutamente il
     * punto operazione più vicino tra quelli rimanenti e lo aggiunge
     * al risultato. Ogni K operazioni si seleziona il punto di reset
     * più vicino e lo si aggiunge al risultato. L'algoritmo termina
     * quando vengono inseriti tutti i punti operazione.
     *
     * @param operationPoints I punti operazione da visitare
     * @param resetPoints I possibili punti di reset
     * @return La lista di punti nell'ordine di visita
     */
    public static List<Point> nearestNeighbor(List<Point> operationPoints, List<Point> resetPoints) {
        List<Point> result = new ArrayList<>();
        int operationsDone = 0;
        List<Point> remainingOperations = new ArrayList<>(operationPoints);
        Point currentPosition = new Point(-1, "X", 0, 0);

        while (!remainingOperations.isEmpty()) {
            if (operationsDone < Constants.K) {
                Point closestOperation = Distance.findClosestPoint(currentPosition, remainingOperations);
                result.add(closestOperation);

                currentPosition = closestOperation;
                remainingOperations.remove(closestOperation);

                operationsDone++;
            } else {
                Point closestReset = Distance.findClosestPoint(currentPosition, resetPoints);
                result.add(closestReset);

                currentPosition = closestReset;

                operationsDone = 0;
            }
        }

        return result;
    }

}
