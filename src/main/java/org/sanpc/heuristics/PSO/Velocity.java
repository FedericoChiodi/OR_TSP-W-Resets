package org.sanpc.heuristics.PSO;

import org.sanpc.model.Point;
import org.sanpc.model.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Velocity(List<Move> moves) {

    /**
     * Applica le mosse della classe una a una alla
     * route passata come parametro
     *
     * @param route La route su cui applicare le mosse
     * @return La route risultante dopo l'applicazione delle mosse
     */
    public Route apply(Route route) {
        Route newRoute = new Route(route);

        for (Move move : moves) {
            newRoute = swap(newRoute, move.index1(), move.index2());
        }

        return newRoute;
    }

    /**
     * Scambia due punti in una route
     *
     * @param route La route da cui si vogliono scambiare i punti
     * @param index1 L'indice del primo punto da scambiare
     * @param index2 L'indice del secondo punto da scambiare
     * @return La nuova route dopo l'applicazione dello scambio
     * @throws IllegalStateException Gli indici non corrispondono a nessun punto nella route
     */
    private Route swap(Route route, int index1, int index2) throws IllegalStateException {
        List<Point> points = new ArrayList<>(route.getPoints());

        try {
            Collections.swap(points, index1, index2);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("Failed to swap points due to invalid indices!", e);
        }

        return new Route(points);
    }

}
