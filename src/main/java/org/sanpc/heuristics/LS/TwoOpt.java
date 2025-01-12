package org.sanpc.heuristics.LS;

import org.sanpc.model.Point;
import org.sanpc.utils.Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoOpt {

    /**
     * Applica la 2-opt alla lista di punti ordinati secondo l'ordine di
     * visita. Viene considerato anche l'origine nell'ottimizzazione
     *
     * @param points La lista ordinata di punti su cui si vuole applicare la 2 opt
     * @return La route ottimizzata
     */
    public static List<Point> apply2OptImprovement(List<Point> points) {
        List<Point> routeWithOrigin = new ArrayList<>();
        routeWithOrigin.add(new Point(-1, "X", 0, 0));
        routeWithOrigin.addAll(points);

        boolean improved;
        do {
            improved = false;
            double bestGain = 0;
            int bestI = -1;
            int bestJ = -1;

            for (int i = 0; i < routeWithOrigin.size() - 1; i++) {
                for (int j = i + 2; j < routeWithOrigin.size(); j++) {
                    double gain = calculate2OptGain(routeWithOrigin, i, j);
                    if (gain > bestGain) {
                        bestGain = gain;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }

            if (bestGain > 0) {
                improved = true;
                perform2OptSwap(routeWithOrigin, bestI, bestJ);
            }
        } while (improved);

        // Rimuove l'origine
        return new ArrayList<>(routeWithOrigin.subList(1, routeWithOrigin.size()));
    }

    /**
     * Calcola il guadagno che deriva da una mossa dalla 2-opt
     *
     * @param points I punti che compongono il percorso totale
     * @param i Il primo indice che coinvolge la mossa
     * @param j Il secondo indice che coinvolge la mossa
     * @return Il guadagno dopo aver simulato la mossa (positivo nel caso di riduzione del percorso totale, negativo o nullo altrimenti)
     */
    private static double calculate2OptGain(List<Point> points, int i, int j) {
        Point p1 = points.get(i);
        Point p2 = points.get(i + 1);
        Point p3 = points.get(j);
        Point p4 = points.get((j + 1) % points.size());

        double currentDistance = Distance.euclideanDistance(p1, p2) + Distance.euclideanDistance(p3, p4);
        double newDistance = Distance.euclideanDistance(p1, p3) + Distance.euclideanDistance(p2, p4);

        return currentDistance - newDistance;
    }

    /**
     * Effettua l'aggiornamento dei punti del percorso applicando
     * la mossa della 2-opt
     *
     * @param points I punti che compongono il percorso totale
     * @param i      Il primo indice che coinvolge la mossa
     * @param j      Il secondo indice che coinvolge la mossa
     */
    private static void perform2OptSwap(List<Point> points, int i, int j) {
        int left = i + 1;
        int right = j;
        while (left < right) {
            Collections.swap(points, left, right);
            left++;
            right--;
        }
    }

}