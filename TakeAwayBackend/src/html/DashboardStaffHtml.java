package html;

/**
 * ============================================================
 * CLASSE DASHBOARD STAFF HTML
 * ============================================================
 *
 * Contiene le parti statiche della pagina HTML
 * della Dashboard Staff.
 *
 * Le righe della tabella e i pulsanti di cambio stato
 * vengono inseriti dinamicamente dal DashboardStaffHandler.
 *
 * La struttura è divisa in più sezioni per:
 * - mantenere l'HTML leggibile
 * - evitare codice HTML direttamente negli handler
 * - facilitare manutenzione e modifiche grafiche
 */
public class DashboardStaffHtml {

    // ====================================================
    // PARTE 1 — HEAD + NAVBAR + APERTURA TABELLA
    // ====================================================

    /**
     * Intestazione HTML:
     * - doctype
     * - head con Bootstrap
     * - navbar staff
     * - apertura tabella ordini
     */
    public static final String DASHBOARD_PARTE_1 = """
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <title>Dashboard Staff</title>

                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

                <style>
                    body {
                        background-color: #f4f6f8;
                    }

                    .stato-btn {
                        margin: 2px;
                    }

                    .stato-attivo {
                        pointer-events: none;
                    }
                </style>
            </head>

            <body>

            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container">
                    <span class="navbar-brand fw-bold">
                        🧑‍🍳 Dashboard Staff
                    </span>

                    <div class="ms-auto">
                        <a href="/" class="btn btn-outline-light btn-sm">
                            Torna al menu
                        </a>
                    </div>
                </div>
            </nav>

            <div class="container mt-4">

                <table class="table table-bordered table-striped align-middle shadow-sm">
                    <thead class="table-dark">
                        <tr>
                            <th>Codice</th>
                            <th>Cliente</th>
                            <th>Totale</th>
                            <th>Stato</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
            """;

    // ====================================================
    // PARTE 2 — CHIUSURA TABELLA E PAGINA
    // ====================================================

    /**
     * Chiusura della tabella e del documento HTML.
     */
    public static final String DASHBOARD_PARTE_2 = """
                    </tbody>
                </table>
            </div>

            </body>
            </html>
            """;
}
