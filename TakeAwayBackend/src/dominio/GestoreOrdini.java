package dominio;

import database.GestoreDatabase;
import database.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * ============================================================
 * CLASSE GESTORE ORDINI
 * ============================================================
 *
 * Questa classe gestisce tutta la logica relativa agli ordini:
 * - creazione di un ordine a partire dal carrello
 * - salvataggio nel database
 * - lettura degli ordini
 * - aggiornamento dello stato
 *
 * La classe funge da collegamento tra:
 * - handler HTTP
 * - dominio
 * - database
 */
public class GestoreOrdini {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Gestore delle connessioni al database.
     */
    private GestoreDatabase gestoreDatabase;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore del gestore ordini.
     *
     * @param gestoreDatabase gestore delle connessioni al database
     */
    public GestoreOrdini(GestoreDatabase gestoreDatabase) {
        this.gestoreDatabase = gestoreDatabase;
    }

    // ====================================================
    // CREAZIONE ORDINE
    // ====================================================

    /**
     * Crea un ordine partendo dal carrello.
     *
     * Il flusso è il seguente:
     * 1) calcolo del totale
     * 2) inserimento ordine nel database
     * 3) recupero ID generato
     * 4) generazione codice ordine leggibile
     * 5) aggiornamento codice ordine nel DB
     * 6) inserimento righe ordine
     * 7) svuotamento carrello
     *
     * @param carrello    carrello del cliente
     * @param nomeCliente nome del cliente
     * @param contatto    contatto del cliente
     * @param note        eventuali note
     * @return ordine creato
     */
    public Ordine creaOrdine(
            Carrello carrello,
            String nomeCliente,
            String contatto,
            String note
    ) {

        double totale = carrello.getTotale();

        // Codice temporaneo (verrà aggiornato dopo l'inserimento)
        Ordine ordine = new Ordine(
                "TEMP",
                nomeCliente,
                contatto,
                note,
                totale
        );

        try (Connection conn = gestoreDatabase.getConnessione()) {

            // ====================================================
            // 1️⃣ Inserimento ordine
            // ====================================================

            PreparedStatement psOrdine = conn.prepareStatement(
                    Query.INSERT_ORDINE,
                    Statement.RETURN_GENERATED_KEYS
            );

            psOrdine.setString(1, ordine.getCodice());
            psOrdine.setString(2, ordine.getNomeCliente());
            psOrdine.setString(3, ordine.getContatto());
            psOrdine.setString(4, ordine.getNote());
            psOrdine.setDouble(5, ordine.getTotale());
            psOrdine.setString(6, ordine.getStato().name());

            psOrdine.executeUpdate();

            // ====================================================
            // 2️⃣ Recupero ID generato
            // ====================================================

            ResultSet chiavi = psOrdine.getGeneratedKeys();
            chiavi.next();
            int ordineId = chiavi.getInt(1);

            // ====================================================
            // 3️⃣ Creazione codice ordine leggibile
            // ====================================================

            String codice = "ORD-" + ordineId;

            // ====================================================
            // 4️⃣ Aggiornamento codice nel database
            // ====================================================

            PreparedStatement psUpdateCodice =
                    conn.prepareStatement("UPDATE ordine SET codice = ? WHERE id = ?");
            psUpdateCodice.setString(1, codice);
            psUpdateCodice.setInt(2, ordineId);
            psUpdateCodice.executeUpdate();

            // ====================================================
            // 5️⃣ Aggiornamento oggetto Java
            // ====================================================

            ordine.setCodice(codice);
            ordine.setId(ordineId);

            // ====================================================
            // 6️⃣ Inserimento righe ordine
            // ====================================================

            PreparedStatement psRiga =
                    conn.prepareStatement(Query.INSERT_RIGA_ORDINE);

            for (RigaCarrello riga : carrello.getRighe()) {
                psRiga.setInt(1, ordineId);
                psRiga.setString(2, riga.getProdotto().getId());
                psRiga.setInt(3, riga.getQuantita());
                psRiga.setDouble(4, riga.getProdotto().getPrezzo());
                psRiga.executeUpdate();
            }

            // ====================================================
            // 7️⃣ Svuotamento carrello
            // ====================================================

            carrello.svuota();

        } catch (SQLException e) {
            System.err.println("Errore durante la creazione dell'ordine");
            e.printStackTrace();
        }

        return ordine;
    }

    // ====================================================
    // LETTURA ORDINI
    // ====================================================

    /**
     * Recupera tutti gli ordini presenti nel database.
     *
     * @return lista di ordini
     */
    public List<Ordine> getTuttiOrdini() {

        List<Ordine> ordini = new ArrayList<>();

        try {

            Connection conn = gestoreDatabase.getConnessione();
            PreparedStatement ps =
                    conn.prepareStatement(Query.SELECT_TUTTI_ORDINI);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String codice = rs.getString("codice");
                String nomeCliente = rs.getString("nome_cliente");
                String contatto = rs.getString("contatto");
                double totale = rs.getDouble("totale");
                StatoOrdine stato =
                        StatoOrdine.valueOf(rs.getString("stato"));
                LocalDateTime data =
                        rs.getTimestamp("data_creazione").toLocalDateTime();
                
                String note = rs.getString("note");
                Ordine ordine = new Ordine(
                        id,
                        codice,
                        nomeCliente,
                        contatto,
                        note,
                        totale,
                        stato,
                        data
                );

                ordini.add(ordine);
            }

        } catch (SQLException e) {
            System.err.println("Errore nel caricamento degli ordini");
            e.printStackTrace();
        }

        return ordini;
    }

    // ====================================================
    // AGGIORNAMENTO STATO ORDINE
    // ====================================================

    /**
     * Cambia lo stato di un ordine verificando
     * che la transizione sia consentita.
     *
     * @param ordineId   id dell'ordine
     * @param nuovoStato nuovo stato desiderato
     * @return true se aggiornamento riuscito, false altrimenti
     */
    public boolean aggiornaStatoOrdine(
            int ordineId,
            StatoOrdine nuovoStato
    ) {

        try (Connection conn = gestoreDatabase.getConnessione()) {

            // ====================================================
            // 1️⃣ Recupero stato attuale
            // ====================================================

            PreparedStatement psSelect =
                    conn.prepareStatement("SELECT stato FROM ordine WHERE id = ?");
            psSelect.setInt(1, ordineId);

            ResultSet rs = psSelect.executeQuery();

            if (!rs.next()) {
                return false;
            }

            StatoOrdine statoAttuale =
                    StatoOrdine.valueOf(rs.getString("stato"));

            // ====================================================
            // 2️⃣ Verifica transizione di stato
            // ====================================================

            if (!statoAttuale.puoPassareA(nuovoStato)) {
                System.err.println(
                        "Transizione NON consentita: "
                                + statoAttuale + " -> " + nuovoStato
                );
                return false;
            }

            // ====================================================
            // 3️⃣ Aggiornamento database
            // ====================================================

            PreparedStatement psUpdate =
                    conn.prepareStatement(Query.UPDATE_STATO_ORDINE);

            psUpdate.setString(1, nuovoStato.name());
            psUpdate.setInt(2, ordineId);

            psUpdate.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("Errore aggiornamento stato ordine");
            e.printStackTrace();
            return false;
        }
    }

    // ====================================================
    // RECUPERO ORDINE PER CODICE
    // ====================================================

    /**
     * Recupera un ordine tramite il codice ordine.
     * Usato dal cliente per monitorare lo stato.
     *
     * @param codice codice ordine mostrato al cliente
     * @return Ordine trovato oppure null se non esiste
     */
    public Ordine getOrdineByCodice(String codice) {

        Ordine ordine = null;

        try (
                Connection conn = gestoreDatabase.getConnessione();
                PreparedStatement ps =
                        conn.prepareStatement(Query.SELECT_ORDINE_BY_CODICE)
        ) {

            ps.setString(1, codice);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                ordine = new Ordine(
                        rs.getInt("id"),
                        rs.getString("codice"),
                        rs.getString("nome_cliente"),
                        rs.getString("contatto"),
                        rs.getString("note"),
                        rs.getDouble("totale"),
                        StatoOrdine.valueOf(rs.getString("stato")),
                        rs.getTimestamp("data_creazione")
                                .toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            System.err.println("Errore nel recupero ordine per codice");
            e.printStackTrace();
        }

        return ordine;
    }
}
