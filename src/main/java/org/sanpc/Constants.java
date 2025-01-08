package org.sanpc;

public class Constants {
    /**
     * Il massimo numero di operazioni che si possono eseguire
     * prima di resettare lo strumento.
     */
    public static final int k = 5;

    /**
     * Il numero di operazioni che lo strumento deve svolgere.
     */
    public static final int nOperations = 20;

    /**
     * Il numero di punti di reset.
     */
    public static final int nResets = 10;

    /**
     * La lunghezza della scheda.
     */
    public static final int length = 50;

    /**
     * La larghezza della scheda.
     */
    public static final int width = 50;

    //-------------//  PSO  //-------------//

    /**
     * Il numero di particelle nel sistema. Tipici
     * valori vanno da 20 a 50, ma possono superare
     * 100 in problemi particolarmente complessi.
     * Aumentare questo numero aumenta il costo
     * computazionale.
     */
    public static final int nParticles = 50;

    /**
     * Fattore di cognizione: quanto una particella è
     * motivata a esplorare soluzioni migliori trovate
     * nel proprio percorso. Tra 1 e 2.5; valori alti
     * incoraggiando a essere indipendenti e seguire la
     * propria strada, mentre valori bassi a seguire lo
     * swarm.
     */
    public static final int c1 = 2;

    /**
     * Fattore di socializzazione: quanto una particella è
     * motivata a esplorare soluzioni migliori trovate
     * dall'intero swarm. Tra 1 e 2.5; valori alti incentivano
     * le particelle a seguire lo swarm, quindi idealmente a
     * convergere a un ottimo globale.
     */
    public static final int c2 = 2;

    public static double maxInertia = 0.9;
    public static double minInertia = 0.4;

    /**
     * Inerzia: quanto la velocità precedente influenza
     * la prossima velocità della particella. Può essere
     * costante ma si preferisce tenerla dinamica da 0.9
     * a 0.4; valori alti significano migliore esplorazione.
     * Dopo un certo tempo si può abbassare dinamicamente.
     */
    public static double inertia = maxInertia;

    /**
     * Il numero massimo di iterazioni che l'algoritmo
     * eseguirà prima di fermarsi e restituire la
     * soluzione migliore trovata fin'ora dallo sciame.
     * Valori tipici vanno da 10000 a 100000, ma possono
     * essere aumentate in problemi specifici. È necessario
     * definire altri criteri di stop come la stabilizzazione
     * di una soluzione o un tempo massimo di esecuzione.
     */
    public static final int maxIterations = 100000;

    /**
     * Il numero iniziale di scambi che la velocità apporterà
     * alle soluzioni. Serve per inizializzare delle velocità
     * casuali rappresentate come una sequenza di scambi, anche
     * non ammissibili inizialmente, alle soluzioni parziali delle
     * singole particelle. Questo parametro non ha dei valori
     * standard e va aggiustato sperimentalmente, dato che
     * dipende dagli altri parametri. Un valore alto porta a
     * una veloce esplorazione che rischia di non convergere,
     * un valore basso porta a pochi scambi, quindi al blocco
     * negli ottimi locali.
     */
    public static final int velocity = 20;

    /**
     * Se permettere o meno di tollerare delle soluzioni non ammissibili
     * per un certo periodo di tempo al fine di aumentare le capacità
     * di ricerca dell'algoritmo.
     */
    public static final boolean doAllowTemporaryInconsistencies = false;

    /**
     * La penalità massima che una soluzione non ammissibile
     * può sopportare prima di essere riparata in termini
     * di numero di iterazioni in cui rimane non ammissibile.
     */
    public static final int maxPenalty = 2;

}
