package dominio;

/*
 * ============================================================
 * CLASSE PRODOTTO
 * ============================================================
 *
 * Questa classe rappresenta un singolo prodotto del menu.
 *
 * Le classi presenti nel package "dominio":
 * - NON conoscono il database
 * - NON conoscono l'HTTP
 *
 * Ogni istanza di Prodotto corrisponde a una riga
 * della tabella "prodotto" nel database.
 */
public class Prodotto {

    // ====================================================
    // ATTRIBUTI (CAMPI DEL PRODOTTO)
    // ====================================================

    /**
     * Identificativo del prodotto (es. PAN1, BEV2).
     */
    private String id;

    /**
     * Nome del prodotto.
     */
    private String nome;

    /**
     * Descrizione del prodotto.
     */
    private String descrizione;

    /**
     * Prezzo del prodotto.
     */
    private double prezzo;

    /**
     * Categoria del prodotto (Panini, Bevande, ecc.).
     */
    private String categoria;

    /**
     * Immagine del prodotto codificata in Base64.
     */
    private String immagine;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore completo del prodotto.
     *
     * @param id          identificativo del prodotto
     * @param nome        nome del prodotto
     * @param descrizione descrizione del prodotto
     * @param prezzo      prezzo del prodotto
     * @param categoria   categoria del prodotto
     * @param immagine    immagine codificata in Base64
     */
    public Prodotto(
            String id,
            String nome,
            String descrizione,
            double prezzo,
            String categoria,
            String immagine
    ) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.categoria = categoria;
        this.immagine = immagine;
    }

    // ====================================================
    // GETTER E SETTER
    // ====================================================

    /**
     * Restituisce l'immagine del prodotto come codice HTML.
     *
     * L'immagine viene incorporata direttamente nella pagina
     * tramite Base64 (data URI).
     *
     * @return StringBuilder contenente il tag <img>
     */
    public StringBuilder getImmagine() {

        StringBuilder html = new StringBuilder();

        html.append("<img alt=\"img ")
            .append("\" src=\"data:image/png;base64,")
            .append(immagine)
            .append("\" style=\"max-width:150px\"/>");

        return html;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // ====================================================
    // METODI DI SUPPORTO
    // ====================================================

    /**
     * Rappresentazione testuale del prodotto.
     * Utilizzata per debug, log o stampa rapida.
     */
    @Override
    public String toString() {
        return nome + " (" + prezzo + "€)";
    }
}
