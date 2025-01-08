package org.sanpc.heuristics.PSO;

import org.sanpc.model.Point;

import java.util.Collections;
import java.util.List;

public record Velocity(List<Move> moves) {

    public Route apply(Route route) {
        Route newRoute = new Route(route);

        for (Move move : moves) {
            newRoute = swap(newRoute, move.index1(), move.index2());
        }

        return newRoute;
    }

    private Route swap(Route route, int index1, int index2) throws IllegalStateException {
        List<Point> points = route.getPoints();

        try {
            Collections.swap(points, index1, index2);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("Failed to swap points due to invalid indices!", e);
        }

        return new Route(points);
    }

}
