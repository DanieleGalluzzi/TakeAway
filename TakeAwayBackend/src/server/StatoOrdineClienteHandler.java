package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dominio.GestoreOrdini;
import dominio.Ordine;
import html.StatoOrdineClienteHtml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * ============================================================
 * HANDLER STATO ORDINE CLIENTE
 * ============================================================
 *
 * Permette al cliente di visualizzare lo stato del proprio ordine
 * tramite il codice ordine.
 *
 * Esempio URL:
 * /ordine/stato?codice=ORD-12
 *
 * Funzionamento:
 * - accetta solo richieste GET
 * - recupera l'ordine tramite codice
 * - mostra i dati dell'ordine oppure un messaggio di errore
 */
public class StatoOrdineClienteHandler implements HttpHandler {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Gestore della logica degli ordini.
     */
    private GestoreOrdini gestoreOrdini;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore dello StatoOrdineClienteHandler.
     *
     * @param gestoreOrdini gestore ordini
     */
    public StatoOrdineClienteHandler(GestoreOrdini gestoreOrdini) {
        this.gestoreOrdini = gestoreOrdini;
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
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // ====================================================
        // LETTURA PARAMETRO CODICE ORDINE
        // ====================================================

        String query = exchange.getRequestURI().getQuery();
        String codice = "";

        if (query != null && query.startsWith("codice=")) {
            codice = query.substring(7);
        }

        // ====================================================
        // RECUPERO ORDINE DAL DATABASE
        // ====================================================

        Ordine ordine = gestoreOrdini.getOrdineByCodice(codice);

        // ====================================================
        // COSTRUZIONE RISPOSTA HTML
        // ====================================================

        StringBuilder html = new StringBuilder();

        if (ordine == null) {

            // --------------------------------
            // ORDINE NON TROVATO
            // --------------------------------
            html.append("""
                <html>
                <body>
                    <h2>Ordine non trovato</h2>
                    <a href="/">Torna al menu</a>
                </body>
                </html>
            """);

        } else {

            // --------------------------------
            // ORDINE TROVATO
            // --------------------------------
            html.append(StatoOrdineClienteHtml.PARTE_1);

            html.append("<p><b>Codice ordine:</b> ")
                .append(ordine.getCodice())
                .append("</p>");

            html.append("<p><b>Stato:</b> ")
                .append("<span class=\"badge bg-info\">")
                .append(ordine.getStato())
                .append("</span></p>");
            
            if (ordine.getNote() != null && !ordine.getNote().isBlank()) {
                html.append("<p><b>Note:</b> ")
                    .append(ordine.getNote())
                    .append("</p>");
            }

            
            html.append("<p><b>Totale:</b> ")
                .append(ordine.getTotale())
                .append(" €</p>");

            html.append(StatoOrdineClienteHtml.PARTE_2);
        }

        // ====================================================
        // INVIO RISPOSTA HTTP
        // ====================================================

        byte[] risposta =
                html.toString().getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, risposta.length);

        OutputStream os = exchange.getResponseBody();
        os.write(risposta);
        os.close();
    }
}
