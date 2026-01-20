package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.GestoreDatabase;
import database.Query;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * ============================================================
 * HANDLER IMMAGINI
 * ============================================================
 *
 * Questo handler si occupa di servire le immagini dei prodotti.
 *
 * Funzionamento:
 * - riceve una richiesta GET con parametro ?id=ID_PRODOTTO
 * - recupera l'immagine dal database (campo LONGBLOB)
 * - la restituisce come risposta HTTP (image/png)
 *
 * L'handler viene utilizzato per caricare dinamicamente
 * le immagini dei prodotti nel menu.
 */
public class ImageHandler implements HttpHandler {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Gestore delle connessioni al database.
     */
    private GestoreDatabase gestoreDatabase;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore dell'ImageHandler.
     *
     * @param gestoreDatabase gestore delle connessioni al database
     */
    public ImageHandler(GestoreDatabase gestoreDatabase) {
        this.gestoreDatabase = gestoreDatabase;
    }

    // ====================================================
    // METODO HANDLE
    // ====================================================

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ====================================================
        // ACCETTAZIONE SOLO RICHIESTE GET
        // ====================================================

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // Metodo non consentito
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // ====================================================
        // LETTURA E VALIDAZIONE PARAMETRO ID
        // ====================================================

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.startsWith("id=")) {
            // Parametro mancante o non valido
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        String idProdotto = query.substring(3);

        // ====================================================
        // RECUPERO IMMAGINE DAL DATABASE
        // ====================================================

        try (
                Connection conn = gestoreDatabase.getConnessione();
                PreparedStatement ps =
                        conn.prepareStatement(Query.SELECT_IMG_PRODOTTO)
        ) {

            ps.setString(1, idProdotto);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                // Prodotto non trovato
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            byte[] immagine = rs.getBytes("immagine");

            if (immagine == null) {
                // Immagine non presente
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            // ====================================================
            // INVIO IMMAGINE COME RISPOSTA HTTP
            // ====================================================

            exchange.getResponseHeaders().set("Content-Type", "image/png");
            exchange.sendResponseHeaders(200, immagine.length);

            OutputStream os = exchange.getResponseBody();
            os.write(immagine);
            os.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
