package dominio;

/*
 * ============================================================
 * CLASSE RIGA CARRELLO
 * ============================================================
 *
 * Questa classe rappresenta una singola riga del carrello:
 * - un prodotto
 * - la quantità selezionata dal cliente
 *
 * Ogni RigaCarrello è quindi l'associazione tra
 * un Prodotto e il numero di pezzi scelti.
 */
public class RigaCarrello {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Prodotto associato alla riga del carrello.
     */
    private Prodotto prodotto;

    /**
     * Quantità del prodotto selezionata.
     */
    private int quantita;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Crea una nuova riga del carrello partendo da:
     * - un prodotto
     * - una quantità iniziale
     *
     * @param prodotto prodotto associato alla riga
     * @param quantita quantità iniziale del prodotto
     */
    public RigaCarrello(Prodotto prodotto, int quantita) {
        super();
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    // ====================================================
    // GETTER E SETTER
    // ====================================================

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    // ====================================================
    // METODI DI SUPPORTO
    // ====================================================

    /**
     * Calcola il totale della riga del carrello.
     *
     * @return prezzo unitario del prodotto moltiplicato
     *         per la quantità
     */
    public double getTotale() {
        return prodotto.getPrezzo() * quantita;
    }
}
