package org.sanpc.heuristics.PSO;

import org.sanpc.model.Point;

import java.util.ArrayList;
import java.util.List;

import static org.sanpc.utils.Distance.euclideanDistance;

public class Route {
    private final List<Point> points;
    private final double length;

    public Route(List<Point> points) {
        this.points = points;
        this.length = calculateTotalLength(points);
    }

    public Route(Route route) {
        this.points = new ArrayList<>(route.points);;
        this.length = route.length;
    }

    /**
     * Calcola la somma delle distanze tra tutti i punti della sequenza
     * e aggiorna il valore di distance corrispondente. Viene compresa
     * anche la distanza iniziale e finale rispetto all'origine.
     */
    private double calculateTotalLength(List<Point> points) {
        double length = 0;
        Point origin = new Point(0,"X",0,0);

        length += euclideanDistance(origin, points.getFirst());
        for(int i = 0; i < points.size() - 1; i++) {
            length += euclideanDistance(points.get(i), points.get(i + 1));
        }
        length += euclideanDistance(points.getLast(),origin);

        return length;
    }

    @Override
    public String toString() {
        return "Route{" +
                "points=" + points +
                ", length=" + length +
                '}';
    }

    // Getters
    public List<Point> getPoints() {
        return points;
    }
    public double getLength() {
        return length;
    }

}
