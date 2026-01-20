package html;

/**
 * ============================================================
 * CLASSE STATO ORDINE CLIENTE HTML
 * ============================================================
 *
 * Contiene le parti statiche della pagina HTML
 * per la visualizzazione dello stato ordine lato cliente.
 *
 * I dati dinamici dell'ordine (codice, stato, totale, ecc.)
 * vengono inseriti dallo StatoOrdineClienteHandler
 * tra le varie sezioni HTML.
 */
public class StatoOrdineClienteHtml {

    // ====================================================
    // PARTE 1 — HEAD + NAVBAR + APERTURA CONTENUTO
    // ====================================================

    /**
     * Intestazione HTML:
     * - doctype
     * - head con Bootstrap
     * - navbar di riepilogo
     * - apertura card contenuto
     */
    public static final String PARTE_1 = """
        <!DOCTYPE html>
        <html lang="it">
        <head>
            <meta charset="UTF-8">
            <title>Stato Ordine</title>

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

            <style>
                body {
                    background-color: #f4f6f8;
                }
            </style>
        </head>

        <body>

        <nav class="navbar navbar-dark bg-dark">
            <div class="container">
                <span class="navbar-brand fw-bold">
                    📦 Stato Ordine
                </span>
            </div>
        </nav>

        <div class="container mt-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="card-title mb-3">
                        Riepilogo ordine
                    </h4>
        """;

    // ====================================================
    // PARTE 2 — CHIUSURA PAGINA
    // ====================================================

    /**
     * Chiusura della card, pulsante di ritorno
     * al menu e chiusura del documento HTML.
     */
    public static final String PARTE_2 = """
                </div>
            </div>

            <div class="text-center mt-4">
                <a href="/" class="btn btn-primary">
                    Torna al menu
                </a>
            </div>
        </div>

        </body>
        </html>
        """;
}
