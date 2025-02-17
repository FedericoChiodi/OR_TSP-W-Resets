package org.sanpc;

import org.sanpc.model.Point;

import java.util.*;

public class Board {
    private final int length;
    private final int width;
    private final int k;
    private final List<Point> operationPoints;
    private final List<Point> resetPoints;

    public Board(int length, int width, int k, int operationSize, int resetPointSize) throws IllegalArgumentException {
        if ((length + 1) * (width + 1) - 1 < resetPointSize + operationSize) {
            throw new IllegalArgumentException("The board is not big enough to host all operation and reset points!");
        }

        this.length = length;
        this.width = width;
        this.k = k;

        List<Point> allPoints = new ArrayList<>();
        allPoints.add(new Point(-1, "X", 0,0 ));
        resetPoints = new ArrayList<>(generatePoints(resetPointSize, width, length, 0, "R", allPoints));
        allPoints.addAll(resetPoints);
        operationPoints = new ArrayList<>(generatePoints(operationSize, width, length, resetPoints.size(), "O", allPoints));
    }

    private List<Point> generatePoints(int nPoints, int maxX, int maxY, int startingID, String type, List<Point> existingPoints) {
        List<Point> points = new ArrayList<>();
        Random random = new Random();
        int currentID = startingID;
        int maxAttempts = 1000 * nPoints;
        int attempts = 0;

        while (points.size() < nPoints && attempts < maxAttempts) {
            int x = random.nextInt(maxX + 1);
            int y = random.nextInt(maxY + 1);
            Point point = new Point(currentID, type, x, y);
            attempts++;

            if (!existingPoints.contains(point)) {
                points.add(point);
                existingPoints.add(point);
                currentID++;
            }
        }

        if (points.size() < nPoints) {
            throw new IllegalStateException("Impossibile generare " + nPoints + " punti univoci dopo " + maxAttempts + " tentativi.");
        }

        return points;
    }

    // Getters
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public int getK() {
        return k;
    }
    public List<Point> getOperationPoints() {
        return operationPoints;
    }
    public List<Point> getResetPoints() {
        return resetPoints;
    }
}