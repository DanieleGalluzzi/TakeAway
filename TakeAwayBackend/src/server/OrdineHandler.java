package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dominio.Carrello;
import dominio.GestoreOrdini;
import dominio.Ordine;
import html.OrdineConfermatoHtml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/*
 * ============================================================
 * HANDLER ORDINE
 * ============================================================
 *
 * Gestisce la conferma di un ordine.
 *
 * Flusso principale:
 * - riceve i dati del form carrello (POST)
 * - crea e salva l'ordine nel database
 * - restituisce la pagina di conferma ordine
 *
 * Questo handler viene invocato dalla submit del form
 * presente nella pagina del carrello.
 */
public class OrdineHandler implements HttpHandler {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Gestore della logica degli ordini.
     */
    private GestoreOrdini gestoreOrdini;

    /**
     * Carrello associato alla sessione corrente.
     */
    private Carrello carrello;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore dell'OrdineHandler.
     *
     * @param gestoreOrdini gestore ordini
     * @param carrello      carrello del cliente
     */
    public OrdineHandler(GestoreOrdini gestoreOrdini, Carrello carrello) {
        this.gestoreOrdini = gestoreOrdini;
        this.carrello = carrello;
    }

    // ====================================================
    // METODO HANDLE
    // ====================================================

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ====================================================
        // ACCETTAZIONE SOLO RICHIESTE POST
        // ====================================================

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            // Metodo non consentito
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // ====================================================
        // LETTURA DATI FORM (POST)
        // ====================================================

        // Lettura body della richiesta HTTP
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String nome = "";
        String contatto = "";
        String note = "";

        // Separazione dei parametri del form
        String[] parametri = body.split("&");

        for (String parametro : parametri) {

            String[] coppia = parametro.split("=");

            if (coppia.length != 2) {
                continue;
            }

            String chiave = coppia[0];
            String valore =
                    URLDecoder.decode(coppia[1], StandardCharsets.UTF_8);

            // Assegnazione valori alle variabili
            switch (chiave) {
                case "nome":
                    nome = valore;
                case "contatto":
                    contatto = valore;
                case "note":
                    note = valore;
            }
        }

        // ====================================================
        // CREAZIONE E SALVATAGGIO ORDINE
        // ====================================================

        Ordine ordine =
                gestoreOrdini.creaOrdine(carrello, nome, contatto, note);

        // ====================================================
        // COSTRUZIONE RISPOSTA HTML
        // ====================================================

        StringBuilder html = new StringBuilder();

        // PARTE 1 — intestazione e apertura contenuto
        html.append(OrdineConfermatoHtml.ORDINE_OK_PARTE_1);

        // -------------------------------
        // PARTE DINAMICA — DATI ORDINE
        // -------------------------------

        html.append("<p><b>Codice ordine:</b> ")
            .append(ordine.getCodice())
            .append("</p>");

        html.append("<p>Totale: <b>")
            .append(ordine.getTotale())
            .append(" €</b></p>");

        html.append("<p>")
            .append("<a href=\"/ordine/stato?codice=")
            .append(ordine.getCodice())
            .append("\">")
            .append("Controlla lo stato del tuo ordine")
            .append("</a>")
            .append("</p>");

        html.append("""
                <p>Grazie per il tuo ordine!</p>
            """);

        // PARTE 2 — chiusura pagina
        html.append(OrdineConfermatoHtml.ORDINE_OK_PARTE_2);

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
