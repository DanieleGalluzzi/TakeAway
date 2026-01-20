package html;

/**
 * ============================================================
 * CLASSE MENU HTML
 * ============================================================
 *
 * Contiene le parti statiche della pagina HTML del menu.
 *
 * Le card dei prodotti vengono inserite dinamicamente
 * dal MenuHandler.
 *
 * Questa classe permette di:
 * - separare HTML dalla logica Java
 * - mantenere il codice più leggibile
 * - facilitare modifiche grafiche future
 */
public class MenuHtml {

    // ====================================================
    // PARTE 1 — HEAD + NAVBAR + APERTURA CONTENUTO
    // ====================================================

    /**
     * Intestazione HTML completa:
     * - doctype
     * - head con Bootstrap
     * - navbar principale
     * - apertura contenitore card prodotti
     */
    public static final String MENU_PARTE_1 = """
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <title>Takeaway Express - Menu</title>

                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

                <style>
                    body {
                        background-color: #f4f6f8;
                    }

                    .card-img-top {
                        height: 180px;
                        object-fit: contain;
                        background-color: #ffffff;
                        padding: 10px;
                    }

                    .prezzo {
                        font-size: 1.1rem;
                        font-weight: bold;
                    }
                </style>
            </head>

            <body>

            <!-- =========================
                 NAVBAR
                 ========================= -->
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container">
                    <span class="navbar-brand fw-bold">
                        🍔 Takeaway Express - Menu
                    </span>

                    <div class="ms-auto d-flex gap-2">
                        <a href="/carrello" class="btn btn-success btn-sm">
                            Carrello
                        </a>

                        <a href="/staff/login" class="btn btn-outline-light btn-sm">
                            Area Staff
                        </a>
                    </div>
                </div>
            </nav>

            <!-- =========================
                 CONTENUTO MENU
                 ========================= -->
            <div class="container mt-4">
                <div class="row g-4">
            """;

    // ====================================================
    // PARTE 2 — CONTROLLO ORDINE + FOOTER + CHIUSURA
    // ====================================================

    /**
     * Chiusura contenuto prodotti,
     * sezione controllo stato ordine
     * e chiusura del documento HTML.
     */
    public static final String MENU_PARTE_2 = """
                </div>
            </div>

            <!-- =========================
                 CONTROLLO STATO ORDINE
                 ========================= -->
            <div class="row justify-content-center mt-5">
                <div class="col-md-6">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h5 class="card-title text-center mb-3">
                                Controlla il tuo ordine
                            </h5>

                            <form action="/ordine/stato" method="get" class="d-flex gap-2">
                                <input type="text"
                                       name="codice"
                                       class="form-control"
                                       placeholder="Inserisci codice ordine"
                                       required>

                                <button type="submit" class="btn btn-primary">
                                    Controlla
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <footer class="text-center mt-5 mb-3 text-muted">
                © Takeaway Express
            </footer>

            </body>
            </html>
            """;
}
