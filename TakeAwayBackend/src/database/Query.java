package database;

/*
 * ============================================================
 * CLASSE QUERY
 * ============================================================
 *
 * Questa classe contiene TUTTE le query SQL del progetto
 * TakeawayExpress.
 *
 * È una classe di utilità:
 * - NON deve essere istanziata
 * - contiene solo costanti statiche
 *
 * Ogni query è definita come String costante e viene
 * richiamata dalle classi di accesso ai dati (DAO / manager).
 *
 * Questo approccio:
 * - centralizza le query
 * - facilita manutenzione e modifiche
 * - evita duplicazioni di SQL nel codice
 */
public class Query {

    // ============================================================
    // ======================== PRODOTTI ==========================
    // ============================================================

    /**
     * Seleziona tutti i prodotti del menu.
     * I risultati sono ordinati per categoria e nome.
     */
    public static final String SELECT_TUTTI_PRODOTTI = """
            SELECT id, nome, descrizione, prezzo, categoria, immagine
            FROM prodotto
            ORDER BY categoria, nome
            """;

    /**
     * Seleziona solo l'immagine di un prodotto.
     * Utilizzata nel menu per caricare dinamicamente le card.
     */
    public static final String SELECT_IMG_PRODOTTO = """
            SELECT immagine
            FROM prodotto
            WHERE id = ?
            """;

    /**
     * Seleziona l'id e l'immagine di tutti i prodotti.
     * Usata per operazioni di recupero immagini multiple.
     */
    public static final String SELECT_TUTTI_ID = """
            SELECT id, immagine
            FROM prodotto
            """;

    /**
     * Seleziona un singolo prodotto partendo dal suo ID.
     */
    public static final String SELECT_PRODOTTO_PER_ID = """
            SELECT id, nome, descrizione, prezzo, categoria
            FROM prodotto
            WHERE id = ?
            """;

    // ============================================================
    // ========================= ORDINI ===========================
    // ============================================================

    /**
     * Inserisce un nuovo ordine nella tabella ordine.
     */
    public static final String INSERT_ORDINE = """
            INSERT INTO ordine
            (codice, nome_cliente, contatto, note, totale, stato)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    /**
     * Inserisce una riga ordine (prodotto + quantità).
     */
    public static final String INSERT_RIGA_ORDINE = """
            INSERT INTO riga_ordine
            (ordine_id, prodotto_id, quantita, prezzo_unitario)
            VALUES (?, ?, ?, ?)
            """;

    /**
     * Seleziona tutti gli ordini.
     * Ordinati dal più recente al più vecchio.
     * Utilizzato nella dashboard dello staff.
     */
    public static final String SELECT_TUTTI_ORDINI = """
    	    SELECT id, codice, nome_cliente, contatto, note, totale, stato, data_creazione
    	    FROM ordine
    	    ORDER BY data_creazione DESC
    	    """;


    /**
     * Seleziona un ordine tramite il suo ID.
     */
    public static final String SELECT_ORDINE_PER_ID = """
            SELECT id, codice, nome_cliente, contatto, note, totale, stato, data_creazione
            FROM ordine
            WHERE id = ?
            """;

    /**
     * Seleziona tutte le righe (prodotti) di un ordine.
     *
     * Alias:
     * - r = riga_ordine
     * - p = prodotto
     */
    public static final String SELECT_RIGHE_PER_ORDINE = """
            SELECT r.prodotto, p.nome, r.quantita, r.prezzo_unitario
            FROM riga_ordine r
            JOIN prodotto p ON r.prodotto_id = p.id
            WHERE r.ordine_id = ?
            """;

    /**
     * Recupera un ordine tramite il codice ordine.
     * Utilizzato dal cliente per verificare lo stato.
     */
    public static final String SELECT_ORDINE_BY_CODICE = """
            SELECT *
            FROM ordine
            WHERE codice = ?
            """;

    /**
     * Aggiorna lo stato di un ordine.
     * Aggiorna anche il timestamp di modifica.
     */
    public static final String UPDATE_STATO_ORDINE = """
            UPDATE ordine
            SET stato = ?, data_aggiornamento = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

    // ============================================================
    // ====================== UTENTE STAFF ========================
    // ============================================================

    /**
     * Verifica le credenziali di accesso di un utente staff.
     */
    public static final String SELECT_STAFF_LOGIN = """
            SELECT id, username, nome
            FROM utente_staff
            WHERE username = ? AND password = ?
            """;
}
