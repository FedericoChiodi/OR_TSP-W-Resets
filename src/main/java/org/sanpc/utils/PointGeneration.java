package org.sanpc.utils;

import org.sanpc.model.Point;

import java.util.*;

public class PointGeneration {

    /**
     * Genera dei punti con coordinate univoche
     *
     * @param nPoints Il numero di punti da generare
     * @param maxX Il massimo valore di x
     * @param maxY Il massimo valore di y
     * @param startingID Il valore di partenza per l'assegnamento degli ID ai punti
     * @param type Il tipo dei punti
     * @param allPoints I punti già esistenti
     * @return Una lista di punti in coordinate non sovrapposte ad altri
     */
    public static List<Point> generatePoints(int nPoints, int maxX, int maxY, int startingID, String type, List<Point> allPoints) {
        List<Point> points = new ArrayList<>();
        Set<Point> seenPoints = new HashSet<>(allPoints);
        Random random = new Random();

        for (int i = startingID; i < nPoints; i++) {
            Point point;
            do {
                int x = random.nextInt(maxX);
                int y = random.nextInt(maxY);
                point = new Point(i, type, x, y);
            } while (!seenPoints.add(point));

            points.add(point);
        }

        return points;
    }


}
