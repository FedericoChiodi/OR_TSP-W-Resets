package org.sanpc.utils;

import org.sanpc.model.Point;

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

}
