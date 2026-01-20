package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dominio.GestoreOrdini;
import dominio.Ordine;
import dominio.SessioneStaff;
import dominio.StatoOrdine;
import html.DashboardStaffHtml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/*
 * ============================================================
 * HANDLER DASHBOARD STAFF
 * ============================================================
 *
 * Gestisce la Dashboard dello staff.
 *
 * Funzionalità:
 * - verifica autenticazione staff
 * - visualizzazione lista ordini
 * - cambio stato ordini
 *
 * Ogni ordine è rappresentato da UNA SOLA riga.
 * Le note (se presenti) vengono mostrate in modo compatto
 * sotto il nome del cliente.
 */
public class DashboardStaffHandler implements HttpHandler {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    private GestoreOrdini gestoreOrdini;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    public DashboardStaffHandler(GestoreOrdini gestoreOrdini) {
        this.gestoreOrdini = gestoreOrdini;
    }

    // ====================================================
    // METODO HANDLE
    // ====================================================

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ------------------------------------------------
        // AUTENTICAZIONE STAFF
        // ------------------------------------------------
        if (!SessioneStaff.isLoggato()) {
            exchange.getResponseHeaders().add("Location", "/staff/login");
            exchange.sendResponseHeaders(302, -1);
            return;
        }

        // ------------------------------------------------
        // SOLO GET
        // ------------------------------------------------
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // ------------------------------------------------
        // CAMBIO STATO ORDINE (?stato=...&id=...)
        // ------------------------------------------------
        String query = exchange.getRequestURI().getQuery();

        if (query != null) {

            String[] parametri = query.split("&");
            String nuovoStato = null;
            Integer ordineId = null;

            for (String p : parametri) {
                if (p.startsWith("stato=")) {
                    nuovoStato = p.substring(6);
                } else if (p.startsWith("id=")) {
                    ordineId = Integer.parseInt(p.substring(3));
                }
            }

            if (nuovoStato != null && ordineId != null) {
                gestoreOrdini.aggiornaStatoOrdine(
                        ordineId,
                        StatoOrdine.valueOf(nuovoStato)
                );

                exchange.getResponseHeaders()
                        .add("Location", "/staff/dashboard?ok=true");
                exchange.sendResponseHeaders(302, -1);
                return;
            }
        }

        // ------------------------------------------------
        // CARICAMENTO ORDINI
        // ------------------------------------------------
        List<Ordine> ordini = gestoreOrdini.getTuttiOrdini();

        // ------------------------------------------------
        // COSTRUZIONE HTML
        // ------------------------------------------------
        StringBuilder html = new StringBuilder();

        html.append(DashboardStaffHtml.DASHBOARD_PARTE_1);

        if (query != null && query.contains("ok=true")) {
            html.append("""
                <div class="alert alert-success text-center fw-bold mb-4">
                    Stato ordine aggiornato correttamente
                </div>
            """);
        }

        // ------------------------------------------------
        // RIGHE ORDINI
        // ------------------------------------------------
        for (Ordine ordine : ordini) {

            html.append("<tr>");

            // -------------------------------
            // CODICE
            // -------------------------------
            html.append("<td>")
                .append(ordine.getCodice())
                .append("</td>");

            // -------------------------------
            // CLIENTE + NOTE (INLINE)
            // -------------------------------
            html.append("<td>")
                .append("<div class=\"fw-semibold\">")
                .append(ordine.getNomeCliente())
                .append("</div>");

            String note = ordine.getNote();
            String contatto = ordine.getContatto();

            if (note != null && !note.isBlank()
                    && !note.equalsIgnoreCase(contatto)) {

                html.append("<div class=\"text-muted small mt-1\">")
                    .append("<span class=\"badge bg-secondary me-1\">note</span>")
                    .append(note)
                    .append("</div>");
            }

            html.append("</td>");

            // -------------------------------
            // TOTALE
            // -------------------------------
            html.append("<td>")
                .append(ordine.getTotale())
                .append(" €</td>");

            // -------------------------------
            // STATO (BADGE)
            // -------------------------------
            String badgeClass = switch (ordine.getStato()) {
                case RICEVUTO -> "bg-secondary";
                case IN_PREPARAZIONE -> "bg-primary";
                case PRONTO -> "bg-warning text-dark";
                case CONSEGNATO -> "bg-success";
            };

            html.append("<td>")
                .append("<span class=\"badge ")
                .append(badgeClass)
                .append("\">")
                .append(ordine.getStato().name())
                .append("</span>")
                .append("</td>");

            // -------------------------------
            // AZIONI
            // -------------------------------
            html.append("<td>");

            StatoOrdine statoAttuale = ordine.getStato();
            StatoOrdine prossimoStato = statoAttuale.getProssimoStato();

            for (StatoOrdine stato : StatoOrdine.values()) {

                String classe = switch (stato) {
                    case RICEVUTO -> "secondary";
                    case IN_PREPARAZIONE -> "primary";
                    case PRONTO -> "warning text-dark";
                    case CONSEGNATO -> "success";
                };

                if (stato == statoAttuale) {
                    html.append("<span class=\"btn btn-sm btn-")
                        .append(classe)
                        .append(" me-1 disabled\">")
                        .append(stato.name())
                        .append("</span>");
                }
                else if (stato == prossimoStato) {
                    html.append("<a class=\"btn btn-sm btn-outline-")
                        .append(classe)
                        .append(" me-1\" href=\"/staff/dashboard?stato=")
                        .append(stato.name())
                        .append("&id=")
                        .append(ordine.getId())
                        .append("\">")
                        .append(stato.name())
                        .append("</a>");
                }
                else {
                    html.append("<span class=\"btn btn-sm btn-outline-secondary me-1 disabled\">")
                        .append(stato.name())
                        .append("</span>");
                }
            }

            html.append("</td>");
            html.append("</tr>");
        }

        html.append(DashboardStaffHtml.DASHBOARD_PARTE_2);

        // ------------------------------------------------
        // RISPOSTA HTTP
        // ------------------------------------------------
        byte[] risposta = html.toString().getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, risposta.length);

        OutputStream os = exchange.getResponseBody();
        os.write(risposta);
        os.close();
    }
}
