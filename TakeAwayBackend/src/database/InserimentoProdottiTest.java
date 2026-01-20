package database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * ============================================================
 * INSERIMENTO PRODOTTI (CLASSE DI TEST)
 * ============================================================
 *
 * Questa classe NON fa parte del flusso principale
 * dell'applicazione TakeawayExpress.
 *
 * È una classe di supporto utilizzata una sola volta
 * per popolare la tabella "prodotto" con:
 * - dati testuali
 * - immagini caricate come LONGBLOB
 *
 * La classe:
 * - apre una connessione diretta al database
 * - legge le immagini da filesystem
 * - le inserisce nel database tramite PreparedStatement
 *
 * Dopo l'inserimento iniziale, questa classe NON è
 * più necessaria in produzione.
 */
public class InserimentoProdottiTest {

    // ====================================================
    // CONFIGURAZIONE DATABASE
    // ====================================================

    private static final String URL =
            "jdbc:mysql://localhost:3306/takeaway_express?serverTimezone=Europe/Rome";

    private static final String USER = "user";
    private static final String PASSWORD = "user";

    // ====================================================
    // CONFIGURAZIONE PERCORSO IMMAGINI
    // ====================================================

    /**
     * Percorso locale delle immagini da caricare nel database.
     * Le immagini verranno lette da questo path e salvate
     * nel campo LONGBLOB della tabella prodotto.
     */
    private static final String IMG_PATH =
            "C:/Users/gallu/OneDrive/Desktop/TakeAway/img/";

    // ====================================================
    // METODO MAIN (TEST)
    // ====================================================

    public static void main(String[] args) {

        // Query SQL di inserimento prodotto
        String sql = """
                INSERT INTO prodotto
                (id, nome, descrizione, prezzo, categoria, immagine)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        // Try-with-resources:
        // - chiude automaticamente Connection e PreparedStatement
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            System.out.println("Connessione al database riuscita.");

            // ====================================================
            // INSERIMENTO PRODOTTI - PANINI
            // ====================================================

            inserisci(ps, "PAN1", "Burger Classico",
                    "Panino con hamburger di manzo, lattuga e pomodoro",
                    6.50, "Panini",
                    new File(IMG_PATH + "burgerClassico.png"));

            inserisci(ps, "PAN2", "Burger al Formaggio",
                    "Hamburger di manzo con formaggio fuso",
                    7.00, "Panini",
                    new File(IMG_PATH + "burgerFormaggio.png"));

            inserisci(ps, "PAN3", "Burger al Pollo",
                    "Panino con pollo croccante e salsa",
                    6.80, "Panini",
                    new File(IMG_PATH + "burgerPollo.png"));

            inserisci(ps, "PAN4", "Burger Vegetariano",
                    "Panino con burger vegetale e verdure",
                    6.90, "Panini",
                    new File(IMG_PATH + "burgerVegeteriano.png"));

            // ====================================================
            // INSERIMENTO PRODOTTI - ANTIPASTI
            // ====================================================

            inserisci(ps, "ANT1", "Patatine Fritte",
                    "Porzione di patatine croccanti",
                    3.00, "Antipasti",
                    new File(IMG_PATH + "patatineFritte.png"));

            inserisci(ps, "ANT2", "Anelli di Cipolla",
                    "Anelli di cipolla fritti",
                    3.50, "Antipasti",
                    new File(IMG_PATH + "anelliCipolla.png"));

            inserisci(ps, "ANT3", "Crocchette di Patate",
                    "Crocchette dorate di patate",
                    3.80, "Antipasti",
                    new File(IMG_PATH + "crocchettePatate.png"));

            inserisci(ps, "ANT4", "Bocconcini di Pollo",
                    "Bocconcini di pollo fritti",
                    4.20, "Antipasti",
                    new File(IMG_PATH + "bocconciniPollo.png"));

            // ====================================================
            // INSERIMENTO PRODOTTI - BEVANDE
            // ====================================================

            inserisci(ps, "BEV1", "Cola",
                    "Bibita gassata 33cl",
                    2.50, "Bevande",
                    new File(IMG_PATH + "cola.png"));

            inserisci(ps, "BEV2", "Aranciata",
                    "Bibita all’arancia 33cl",
                    2.50, "Bevande",
                    new File(IMG_PATH + "aranciata.png"));

            inserisci(ps, "BEV3", "Acqua Naturale",
                    "Bottiglia 50cl",
                    1.50, "Bevande",
                    new File(IMG_PATH + "acquaNaturale.png"));

            inserisci(ps, "BEV4", "Acqua Frizzante",
                    "Bottiglia 50cl",
                    1.50, "Bevande",
                    new File(IMG_PATH + "acquaFrizzante.png"));

            System.out.println("Inserimento prodotti completato.");

        } catch (Exception e) {
            // In una classe di test è accettabile stampare lo stack trace
            e.printStackTrace();
        }
    }

    // ====================================================
    // METODO DI SUPPORTO PER L'INSERIMENTO
    // ====================================================

    /**
     * Inserisce un singolo prodotto nel database,
     * caricando l'immagine nel campo LONGBLOB.
     *
     * @param ps           PreparedStatement già configurato
     * @param id           identificativo prodotto
     * @param nome         nome del prodotto
     * @param descrizione  descrizione del prodotto
     * @param prezzo       prezzo del prodotto
     * @param categoria    categoria del prodotto
     * @param immagine     file immagine da caricare
     * @throws Exception   in caso di errori di I/O o SQL
     */
    private static void inserisci(
            PreparedStatement ps,
            String id,
            String nome,
            String descrizione,
            double prezzo,
            String categoria,
            File immagine
    ) throws Exception {

        // Verifica esistenza file immagine
        if (!immagine.exists()) {
            throw new IllegalArgumentException(
                    "File non trovato: " + immagine.getAbsolutePath()
            );
        }

        // Binding parametri SQL
        ps.setString(1, id);
        ps.setString(2, nome);
        ps.setString(3, descrizione);
        ps.setDouble(4, prezzo);
        ps.setString(5, categoria);

        // Lettura immagine e inserimento come stream binario
        try (FileInputStream fis = new FileInputStream(immagine)) {
            ps.setBinaryStream(6, fis, immagine.length());
            ps.executeUpdate();
        }

        System.out.println("Inserito prodotto: " + id);
    }
}
