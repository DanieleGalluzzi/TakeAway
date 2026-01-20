package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dominio.SessioneStaff;
import html.LoginStaffHtml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * ============================================================
 * HANDLER LOGIN STAFF
 * ============================================================
 *
 * Gestisce l'autenticazione dello staff.
 *
 * Funzionamento:
 * - GET  → mostra il form di login
 * - POST → verifica le credenziali inserite
 *
 * Le credenziali sono hard-coded nella classe SessioneStaff.
 */
public class LoginStaffHandler implements HttpHandler {

    // ====================================================
    // METODO HANDLE
    // ====================================================

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ====================================================
        // RICHIESTA GET → VISUALIZZAZIONE FORM LOGIN
        // ====================================================

        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            mostraForm(exchange, "");
            return;
        }

        // ====================================================
        // RICHIESTA POST → VERIFICA CREDENZIALI
        // ====================================================

        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            // Lettura body della richiesta
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String username = "";
            String password = "";

            // Parsing parametri POST (x-www-form-urlencoded)
            for (String p : body.split("&")) {

                String[] coppia = p.split("=");

                if (coppia.length == 2) {

                    String key = coppia[0];
                    String value =
                            URLDecoder.decode(coppia[1], StandardCharsets.UTF_8);

                    if (key.equals("username")) {
                        username = value;
                    }

                    if (key.equals("password")) {
                        password = value;
                    }
                }
            }

            // Verifica credenziali
            boolean ok = SessioneStaff.login(username, password);

            if (ok) {
                // Login corretto → redirect alla dashboard
                exchange.getResponseHeaders()
                        .add("Location", "/staff/dashboard");
                exchange.sendResponseHeaders(302, -1);
            } else {
                // Login errato → mostra form con errore
                mostraForm(exchange, "Credenziali errate");
            }
        }
    }

    // ====================================================
    // METODO DI SUPPORTO — RENDER FORM LOGIN
    // ====================================================

    /**
     * Mostra il form di login staff.
     *
     * @param exchange oggetto HttpExchange
     * @param errore   messaggio di errore da visualizzare
     * @throws IOException in caso di errore I/O
     */
    private void mostraForm(HttpExchange exchange, String errore)
            throws IOException {

        StringBuilder html = new StringBuilder();

        // Parte iniziale della pagina
        html.append(LoginStaffHtml.LOGIN_PARTE_1);

        // Messaggio di errore (solo se presente)
        if (errore != null && !errore.isEmpty()) {
            html.append(LoginStaffHtml.LOGIN_ERRORE.formatted(errore));
        }

        // Form di login
        html.append(LoginStaffHtml.LOGIN_FORM);

        // Chiusura pagina
        html.append(LoginStaffHtml.LOGIN_PARTE_4);

        // Invio risposta HTTP
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
