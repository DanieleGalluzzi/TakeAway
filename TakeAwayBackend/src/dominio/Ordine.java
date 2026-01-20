package dominio;

import java.time.LocalDateTime;

/*
 * ============================================================
 * CLASSE ORDINE
 * ============================================================
 *
 * Questa classe rappresenta un ordine effettuato da un cliente.
 * È la rappresentazione Java della tabella "ordine" del database.
 *
 * L'oggetto Ordine viene utilizzato:
 * - durante la creazione di un nuovo ordine
 * - durante la lettura degli ordini dal database
 * - per la gestione dello stato dell'ordine
 */
public class Ordine {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Identificativo univoco dell'ordine (PK database).
     */
    private int id;

    /**
     * Codice ordine leggibile dal cliente (es. ORD-12).
     */
    private String codice;

    /**
     * Nome del cliente che ha effettuato l'ordine.
     */
    private String nomeCliente;

    /**
     * Contatto del cliente (telefono, email, ecc.).
     */
    private String contatto;

    /**
     * Eventuali note inserite dal cliente.
     */
    private String note;

    /**
     * Totale dell'ordine.
     */
    private double totale;

    /**
     * Stato corrente dell'ordine.
     */
    private StatoOrdine stato;

    /**
     * Data e ora di creazione dell'ordine.
     */
    private LocalDateTime dataCreazione;

    // ====================================================
    // COSTRUTTORI
    // ====================================================

    /**
     * Costruttore utilizzato quando si crea
     * un nuovo ordine a partire dal carrello.
     *
     * In questo caso:
     * - lo stato iniziale è RICEVUTO
     * - la data di creazione è impostata a "now"
     *
     * @param codice       codice temporaneo o definitivo
     * @param nomeCliente  nome del cliente
     * @param contatto     contatto del cliente
     * @param note         eventuali note
     * @param totale       totale dell'ordine
     */
    public Ordine(
            String codice,
            String nomeCliente,
            String contatto,
            String note,
            double totale
    ) {
        this.codice = codice;
        this.nomeCliente = nomeCliente;
        this.contatto = contatto;
        this.note = note;
        this.totale = totale;
        this.stato = StatoOrdine.RICEVUTO;
        this.dataCreazione = LocalDateTime.now();
    }

    /**
     * Costruttore utilizzato quando si legge
     * un ordine già esistente dal database.
     *
     * Tutti i campi vengono ricostruiti
     * a partire dai dati persistiti.
     *
     * @param id             id dell'ordine
     * @param codice         codice ordine
     * @param nomeCliente    nome del cliente
     * @param contatto       contatto del cliente
     * @param note           note dell'ordine
     * @param totale         totale dell'ordine
     * @param stato          stato corrente dell'ordine
     * @param dataCreazione  data e ora di creazione
     */
    public Ordine(
            int id,
            String codice,
            String nomeCliente,
            String contatto,
            String note,
            double totale,
            StatoOrdine stato,
            LocalDateTime dataCreazione
    ) {
        this.id = id;
        this.codice = codice;
        this.nomeCliente = nomeCliente;
        this.contatto = contatto;
        this.note = note;
        this.totale = totale;
        this.stato = stato;
        this.dataCreazione = dataCreazione;
    }

    // ====================================================
    // GETTER E SETTER
    // ====================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getContatto() {
        return contatto;
    }

    public void setContatto(String contatto) {
        this.contatto = contatto;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
}
