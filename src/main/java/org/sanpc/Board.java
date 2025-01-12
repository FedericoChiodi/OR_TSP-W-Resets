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

        resetPoints = generatePoints(resetPointSize, width, length, 0, "R", new HashSet<>());
        operationPoints = generatePoints(operationSize, width, length, resetPoints.size(), "O", new HashSet<>(resetPoints));
    }

    public void printBoard() {
        System.out.println("Length: " + this.length);
        System.out.println("Width: " + this.width);
        System.out.println("K: " + this.k);

        System.out.println("Operation points:");
        for (Point point : operationPoints) {
            System.out.println(point);
        }
        System.out.println();

        System.out.println("Reset points:");
        for (Point point : resetPoints) {
            System.out.println(point);
        }
    }

    private List<Point> generatePoints(int nPoints, int maxX, int maxY, int startingID, String type, Set<Point> existingPoints) {
        List<Point> points = new ArrayList<>();
        Random random = new Random();
        int currentID = startingID;

        Point origin = new Point(-1, "X", 0, 0);
        existingPoints.add(origin);

        while (points.size() < nPoints) {
            int x = random.nextInt(maxX + 1);
            int y = random.nextInt(maxY + 1);
            Point point = new Point(currentID, type, x, y);

            if (!existingPoints.contains(point)) {
                points.add(point);
                existingPoints.add(point);
                currentID++;
            }
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