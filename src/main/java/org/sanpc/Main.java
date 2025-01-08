package org.sanpc;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(Constants.LENGTH, Constants.WIDTH, Constants.K, Constants.N_OPERATIONS, Constants.N_RESETS);
        BoardVisualizer.visualizeBoard(board);
    }
}
