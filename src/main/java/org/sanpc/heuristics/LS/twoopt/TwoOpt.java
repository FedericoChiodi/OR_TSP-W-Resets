package org.sanpc.heuristics.LS.twoopt;

import org.sanpc.model.Point;
import org.sanpc.model.Route;
import org.sanpc.utils.Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoOpt {

    public static List<Point> apply2OptImprovement(List<Point> points) {
        List<Point> routeWithOrigin = new ArrayList<>();
        routeWithOrigin.add(new Point(-1, "X", 0, 0)); // Origin point
        routeWithOrigin.addAll(points);

        boolean improved;
        do {
            improved = false;
            double bestGain = 0;
            int bestI = -1;
            int bestJ = -1;

            for (int i = 0; i < routeWithOrigin.size() - 1; i++) {
                if ("R".equals(routeWithOrigin.get(i).getType())) continue;

                for (int j = i + 2; j < routeWithOrigin.size(); j++) {
                    if ("R".equals(routeWithOrigin.get(j).getType()) || hasResetBetween(routeWithOrigin, i + 1, j - 1)) {
                        continue;
                    }

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

        Route route = new Route(routeWithOrigin);
        System.out.println("Route cost - 2opt: " + route.getLength() + "\n");

        return new ArrayList<>(routeWithOrigin.subList(1, routeWithOrigin.size()));
    }

    private static boolean hasResetBetween(List<Point> points, int from, int to) {
        for (int k = from; k <= to; k++) {
            if ("R".equals(points.get(k).getType())) {
                return true;
            }
        }
        return false;
    }

    private static double calculate2OptGain(List<Point> points, int i, int j) {
        Point p1 = points.get(i);
        Point p2 = points.get(i + 1);
        Point p3 = points.get(j);
        Point p4 = points.get((j + 1) % points.size());

        double currentDistance = Distance.euclideanDistance(p1, p2) + Distance.euclideanDistance(p3, p4);
        double newDistance = Distance.euclideanDistance(p1, p3) + Distance.euclideanDistance(p2, p4);

        return currentDistance - newDistance;
    }

    public static void perform2OptSwap(List<Point> points, int i, int j) {
        int left = i + 1;
        int right = j;
        while (left < right) {
            Collections.swap(points, left, right);
            left++;
            right--;
        }
    }
}