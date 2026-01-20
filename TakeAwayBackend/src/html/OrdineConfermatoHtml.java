package html;

/**
 * ============================================================
 * CLASSE ORDINE CONFERMATO HTML
 * ============================================================
 *
 * Contiene le parti statiche della pagina HTML
 * di conferma ordine.
 *
 * I dati dinamici dell'ordine, come:
 * - codice ordine
 * - totale
 *
 * vengono inseriti dinamicamente
 * dall'OrdineHandler.
 *
 * La separazione in più parti permette
 * di mantenere l'HTML pulito e leggibile.
 */
public class OrdineConfermatoHtml {

    // ====================================================
    // PARTE 1 — HEAD + HEADER + APERTURA CARD
    // ====================================================

    /**
     * Intestazione HTML:
     * - doctype
     * - head con Bootstrap
     * - header di conferma
     * - apertura card contenuto
     */
    public static final String ORDINE_OK_PARTE_1 = """
        <!DOCTYPE html>
        <html lang="it">
        <head>
            <meta charset="UTF-8">
            <title>Ordine confermato</title>

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

            <style>
                body {
                    background-color: #f8f9fa;
                }

                header {
                    background-color: #198754;
                    color: white;
                    padding: 20px;
                    text-align: center;
                }
            </style>
        </head>

        <body>

        <header>
            <h1>Ordine confermato</h1>
            <p>Il tuo ordine è stato ricevuto correttamente</p>
        </header>

        <div class="container mt-4">
            <div class="card shadow-sm">
                <div class="card-body">
        """;

    // ====================================================
    // PARTE 2 — CHIUSURA PAGINA
    // ====================================================

    /**
     * Chiusura della card, pulsante di ritorno
     * al menu e chiusura del documento HTML.
     */
    public static final String ORDINE_OK_PARTE_2 = """
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
