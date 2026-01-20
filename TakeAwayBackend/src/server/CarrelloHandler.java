package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dominio.Carrello;
import dominio.CatalogoProdotti;
import dominio.Prodotto;
import dominio.RigaCarrello;
import html.CarrelloHtml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * ============================================================
 * HANDLER CARRELLO
 * ============================================================
 *
 * Gestisce tutte le richieste relative al carrello:
 * - aggiunta di prodotti al carrello
 * - visualizzazione del contenuto del carrello
 *
 * Questo handler risponde sia a richieste GET che POST.
 */
public class CarrelloHandler implements HttpHandler {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Carrello associato alla sessione corrente.
     */
    private Carrello carrello;

    /**
     * Catalogo prodotti utilizzato per recuperare
     * le informazioni dei prodotti.
     */
    private CatalogoProdotti catalogoProdotti;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore del CarrelloHandler.
     *
     * @param carrello         carrello condiviso
     * @param catalogoProdotti catalogo prodotti
     */
    public CarrelloHandler(Carrello carrello, CatalogoProdotti catalogoProdotti) {
        this.carrello = carrello;
        this.catalogoProdotti = catalogoProdotti;
    }

    // ====================================================
    // METODO HANDLE
    // ====================================================

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ====================================================
        // VALIDAZIONE METODO HTTP
        // ====================================================

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")
                && !exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            // Metodo non consentito
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // ====================================================
        // GESTIONE AGGIUNTA PRODOTTO (?add=ID)
        // ====================================================

        String query = exchange.getRequestURI().getQuery();

        if (query != null && query.startsWith("add=")) {

            String prodottoId = query.substring(4);

            // Ricerca del prodotto nel catalogo
            for (Prodotto p : catalogoProdotti.getTuttiProdotti()) {
                if (p.getId().equals(prodottoId)) {
                    carrello.aggiungiProdotto(p);
                    break;
                }
            }

            // Redirect al menu principale
            exchange.getResponseHeaders().add("Location", "/");
            exchange.sendResponseHeaders(302, -1);
            return;
        }

        // ====================================================
        // COSTRUZIONE PAGINA HTML CARRELLO
        // ====================================================

        StringBuilder html = new StringBuilder();

        // PARTE 1 — intestazione e apertura tabella
        html.append(CarrelloHtml.CARRELLO_PARTE_1);

        // PARTE DINAMICA — righe del carrello
        for (RigaCarrello riga : carrello.getRighe()) {
            html.append("<tr>")
                .append("<td>")
                .append(riga.getProdotto().getNome())
                .append("</td>")
                .append("<td>")
                .append(riga.getQuantita())
                .append("</td>")
                .append("<td>")
                .append(riga.getTotale())
                .append(" €</td>")
                .append("</tr>");
        }

        // PARTE 2 — apertura sezione totale
        html.append(CarrelloHtml.CARRELLO_PARTE_2);

        // INSERIMENTO TOTALE DINAMICO
        html.append(carrello.getTotale()).append(" €");

        // PARTE 3 — chiusura pagina
        html.append(CarrelloHtml.CARRELLO_PARTE_3);

        // ====================================================
        // INVIO RISPOSTA HTTP
        // ====================================================

        byte[] risposta = html.toString().getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, risposta.length);

        OutputStream os = exchange.getResponseBody();
        os.write(risposta);
        os.close();
    }
}
