package org.sanpc.heuristics.LS.tabusearch;

import org.sanpc.model.Point;

import java.util.Objects;

public class Move {
    String type;
    int index1;
    int index2;
    Point point;

    public Move(int i1, int i2) {
        type = "SWAP";
        index1 = i1;
        index2 = i2;
    }

    public Move(int index, Point p, boolean isAdd) {
        type = isAdd ? "ADD" : "REMOVE";
        index1 = index;
        point = p;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move other)) return false;
        if (!Objects.equals(type, other.type)) return false;

        return switch (type) {
            case "SWAP" -> (index1 == other.index1 && index2 == other.index2) ||
                    (index1 == other.index2 && index2 == other.index1);
            case "ADD", "REMOVE" -> index1 == other.index1 && point.equals(other.point);
            default -> false;
        };
    }
}
