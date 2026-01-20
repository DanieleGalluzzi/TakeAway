package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dominio.Carrello;
import dominio.CatalogoProdotti;
import dominio.Prodotto;
import html.MenuHtml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/*
 * ============================================================
 * HANDLER MENU
 * ============================================================
 *
 * Gestisce la rotta principale "/" dell'applicazione.
 *
 * Funzionalità:
 * - recupera il menu prodotti dal database
 * - genera dinamicamente le card dei prodotti
 * - permette l'aggiunta di prodotti al carrello
 */
public class MenuHandler implements HttpHandler {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Catalogo prodotti utilizzato per recuperare
     * i prodotti dal database.
     */
    private CatalogoProdotti catalogoProdotti;

    /**
     * Carrello associato alla sessione corrente.
     */
    private Carrello carrello;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore del MenuHandler.
     *
     * @param catalogoProdotti catalogo prodotti
     * @param carrello         carrello condiviso
     */
    public MenuHandler(CatalogoProdotti catalogoProdotti, Carrello carrello) {
        this.catalogoProdotti = catalogoProdotti;
        this.carrello = carrello;
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
        // CARICAMENTO PRODOTTI DAL DATABASE
        // ====================================================

        List<Prodotto> prodotti = catalogoProdotti.getTuttiProdotti();

        // ====================================================
        // COSTRUZIONE PAGINA HTML MENU
        // ====================================================

        StringBuilder html = new StringBuilder();

        // PARTE 1 — intestazione e apertura contenuto
        html.append(MenuHtml.MENU_PARTE_1);

        // ====================================================
        // PARTE DINAMICA — CARD DEI PRODOTTI
        // ====================================================

        for (Prodotto p : prodotti) {

            html.append("""
                <div class="col-md-4">
                    <div class="card h-100">
                """);

            // Immagine del prodotto (HTML già pronto dal dominio)
            html.append(p.getImmagine());

            html.append("""
                        <div class="card-body">
                            <h5 class="card-title">""")
                .append(p.getNome())
                .append("""
                            </h5>
                            <p class="card-text">""")
                .append(p.getDescrizione())
                .append("""
                            </p>
                            <p class="fw-bold">""")
                .append(p.getPrezzo())
                .append("""
                            €</p>
                            <a href="/carrello?add=""")
                .append(p.getId())
                .append("""
                            " class="btn btn-primary">
                                Aggiungi
                            </a>
                        </div>
                    </div>
                </div>
                """);
        }

        // PARTE 2 — chiusura pagina
        html.append(MenuHtml.MENU_PARTE_2);

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
