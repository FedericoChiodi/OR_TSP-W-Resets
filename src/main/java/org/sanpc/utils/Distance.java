package org.sanpc.utils;

import org.sanpc.model.Point;

import java.util.List;

public class Distance {

    /**
     * Calcola la distanza euclidea tra due punti bidimensionali
     *
     * @param p1 Il primo punto
     * @param p2 Il secondo punto
     * @return La distanza euclidea tra i due punti
     */
    public static double euclideanDistance(Point p1, Point p2) {
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Trova il punto più vicino a un punto di riferimento in una lista di punti,
     * utilizzando la distanza euclidea come criterio di prossimità.
     *
     * @param current Il punto di riferimento rappresentato come una coppia (x, y) di coordinate intere.
     * @param points  La lista dei punti candidati, ciascuno rappresentato come una coppia (x, y) di coordinate intere.
     * @return La coppia (x, y) corrispondente al punto più vicino nella lista rispetto al punto di riferimento.
     *         Restituisce `null` se la lista dei punti è vuota.
     */
    public static Point findClosestPoint(Point current, List<Point> points) {
        Point closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Point point : points) {
            double distance = euclideanDistance(current, point);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        return closestPoint;
    }

}
