package org.sanpc;

/**
 * TESTO:
 * Un braccio robotico
 * monta uno strumento con cui deve eseguire delle operazioni O={o}
 * su una scheda, una alla volta, ciascuna localizzata in un punto di
 * coordinate x_o, y_o. Le operazioni possono essere svolte in
 * qualunque ordine. Dopo al più k operazioni consecutive però,
 * occorre resettare lo strumento, e questo può avvenire in una
 * qualunque delle postazioni dedicate, R={r}, poste nel punto x_r, y_r.
 * Si determini la sequenza di operazioni e resettaggio che minimizza
 * la somma degli spostamenti (distanze euclidee tra punti), sapendo
 * che si parte e si termina in posizione 0,0.
* */

public class Main {
    public static void main(String[] args) {
        Board board = new Board(Constants.LENGTH, Constants.WIDTH, Constants.K, Constants.N_OPERATIONS, Constants.N_RESETS);
        BoardVisualizer.visualizeBoard(board);
    }
}
