package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ============================================================
 * GESTORE DATABASE
 * ============================================================
 *
 * La classe GestoreDatabase ha il compito di fornire
 * una connessione al database MySQL dell'applicazione.
 *
 * CARATTERISTICHE IMPORTANTI:
 * - NON mantiene una connessione persistente
 * - NON conserva stato interno
 * - Ogni chiamata restituisce una NUOVA connessione
 *
 * Questo approccio:
 * - evita problemi di connessioni chiuse o scadute
 * - semplifica la gestione delle risorse
 * - rende la classe sicura e riutilizzabile
 */
public class GestoreDatabase {

    // ====================================================
    // PARAMETRI DI CONNESSIONE
    // ====================================================

    /**
     * URL di connessione al database MySQL.
     * - database: takeaway_express
     * - timezone impostata per evitare warning
     */
    private static final String URL =
            "jdbc:mysql://localhost:3306/takeaway_express?serverTimezone=Europe/Rome";

    /**
     * Nome utente del database
     */
    private static final String USERNAME = "user";

    /**
     * Password del database
     */
    private static final String PASSWORD = "user";

    // ====================================================
    // METODI PUBBLICI
    // ====================================================

    /**
     * Restituisce SEMPRE una nuova connessione al database.
     *
     * La connessione:
     * - deve essere chiusa da chi la utilizza
     * - non viene mai salvata come attributo di classe
     *
     * @return una nuova istanza di {@link Connection}
     * @throws SQLException in caso di errore di connessione
     */
    public Connection getConnessione() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
