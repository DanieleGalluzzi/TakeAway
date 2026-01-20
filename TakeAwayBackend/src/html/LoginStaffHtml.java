package html;

/**
 * ============================================================
 * CLASSE LOGIN STAFF HTML
 * ============================================================
 *
 * Contiene le parti statiche della pagina HTML
 * di login dello staff.
 *
 * Il messaggio di errore viene inserito dinamicamente
 * dal LoginStaffHandler tramite String.format().
 *
 * Questa suddivisione permette di:
 * - mantenere l'HTML separato dalla logica Java
 * - riutilizzare facilmente i blocchi
 * - migliorare leggibilità e manutenzione
 */
public class LoginStaffHtml {

    // ====================================================
    // PARTE 1 — HEAD + HEADER + APERTURA CARD
    // ====================================================

    /**
     * Intestazione HTML:
     * - doctype
     * - head con Bootstrap
     * - header grafico
     * - apertura card di login
     */
    public static final String LOGIN_PARTE_1 = """
        <!DOCTYPE html>
        <html lang="it">
        <head>
            <meta charset="UTF-8">
            <title>Login Staff</title>

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

            <style>
                body {
                    background-color: #f8f9fa;
                }

                header {
                    background-color: #0d6efd;
                    color: white;
                    padding: 20px;
                    text-align: center;
                }

                .login-box {
                    max-width: 400px;
                    margin: auto;
                }
            </style>
        </head>

        <body>

        <header>
            <h1>Area Staff</h1>
            <p>Accesso riservato al personale</p>
        </header>

        <div class="container mt-5">
            <div class="card login-box shadow-sm">
                <div class="card-body">
                    <h4 class="card-title text-center mb-4">
                        Login Staff
                    </h4>
        """;

    // ====================================================
    // PARTE 2 — MESSAGGIO DI ERRORE (DINAMICO)
    // ====================================================

    /**
     * Blocco HTML per la visualizzazione di un messaggio
     * di errore durante il login.
     *
     * Il contenuto (%s) viene sostituito dinamicamente
     * dal LoginStaffHandler.
     */
    public static final String LOGIN_ERRORE = """
        <div class="alert alert-danger text-center">
            %s
        </div>
        """;

    // ====================================================
    // PARTE 3 — FORM DI LOGIN
    // ====================================================

    /**
     * Form HTML per l'inserimento delle credenziali staff.
     */
    public static final String LOGIN_FORM = """
        <form method="post">

            <div class="mb-3">
                <label class="form-label">Username</label>
                <input name="username" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Password</label>
                <input name="password" type="password" class="form-control" required>
            </div>

            <div class="d-grid">
                <button class="btn btn-primary">
                    Accedi
                </button>
            </div>

        </form>
        """;

    // ====================================================
    // PARTE 4 — CHIUSURA PAGINA
    // ====================================================

    /**
     * Chiusura della card, del container
     * e del documento HTML.
     */
    public static final String LOGIN_PARTE_4 = """
                </div>
            </div>
        </div>

        </body>
        </html>
        """;
}
