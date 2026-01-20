package html;

/**
 * ============================================================
 * CLASSE CARRELLO HTML
 * ============================================================
 *
 * Contiene le parti statiche della pagina HTML del carrello.
 *
 * La pagina viene costruita dinamicamente dal CarrelloHandler
 * concatenando queste sezioni con:
 * - le righe del carrello
 * - il totale dell'ordine
 *
 * Questa separazione permette di:
 * - mantenere il codice HTML ordinato
 * - evitare HTML hardcoded negli handler
 * - migliorare leggibilità e manutenzione
 */
public class CarrelloHtml {

    // ====================================================
    // PARTE 1 — HEAD + HEADER + APERTURA TABELLA
    // ====================================================

    /**
     * Intestazione HTML completa:
     * - doctype
     * - head con Bootstrap
     * - header grafico
     * - apertura tabella carrello
     */
    public static final String CARRELLO_PARTE_1 = """
        <!DOCTYPE html>
        <html lang="it">
        <head>
            <meta charset="UTF-8">
            <title>Takeaway Express - Carrello</title>

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

            <style>
                body {
                    background-color: #f8f9fa;
                }

                header {
                    background-color: #212529;
                    color: white;
                    padding: 20px;
                    text-align: center;
                }

                .table th {
                    background-color: #343a40;
                    color: white;
                }
            </style>
        </head>

        <body>

        <header>
            <h1>Il tuo carrello</h1>
            <p>Riepilogo dei prodotti selezionati</p>
        </header>

        <div class="container mt-4">

            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Quantità</th>
                        <th>Totale</th>
                    </tr>
                </thead>
                <tbody>
        """;

    // ====================================================
    // PARTE 2 — BLOCCO TOTALE ORDINE
    // ====================================================

    /**
     * Chiusura della tabella e apertura del blocco
     * che mostra il totale dell'ordine.
     *
     * Il valore del totale viene inserito dinamicamente
     * dal CarrelloHandler.
     */
    public static final String CARRELLO_PARTE_2 = """
                </tbody>
            </table>

            <!-- TOTALE -->
            <div class="text-end mb-4">
                <h4>
                    Totale ordine:
                    <span class="fw-bold">
        """;

    // ====================================================
    // PARTE 3 — FORM CONFERMA + CHIUSURA PAGINA
    // ====================================================

    /**
     * Chiusura del totale, form di conferma ordine
     * e chiusura completa del documento HTML.
     */
    public static final String CARRELLO_PARTE_3 = """
                    </span>
                </h4>
            </div>

            <!-- FORM CONFERMA ORDINE -->
            <form action="/ordine" method="post">

                <div class="mb-3">
                    <label class="form-label">Nome cliente</label>
                    <input type="text" name="nome" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Telefono o Email</label>
                    <input type="text" name="contatto" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Note aggiuntive</label>
                    <textarea name="note" class="form-control" rows="3"></textarea>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="/" class="btn btn-secondary">
                        Aggiungi altri prodotti
                    </a>

                    <button type="submit" class="btn btn-success">
                        Conferma ordine
                    </button>
                </div>

            </form>

        </div>

        </body>
        </html>
        """;
}
