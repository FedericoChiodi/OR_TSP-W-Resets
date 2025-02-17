package org.sanpc.model;

import java.util.Objects;

public class Point {
    private int id;
    /**
     * X = Origine;
     * O = Operazione;
     * R = Reset;
     */
    private String type;
    private int x;
    private int y;

    public Point(int id, String type, int x, int y) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", coordinates={" + x + ", " + y + "}" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && Objects.equals(type, point.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, x, y);
    }
}