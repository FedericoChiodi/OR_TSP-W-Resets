package org.sanpc.heuristics.PSO;

public record Move(int index1, int index2) {

    @Override
    public String toString() {
        return "Move{" +
                ", index1=" + index1 +
                ", index2=" + index2 +
                '}';
    }
}
