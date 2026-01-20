package dominio;

import java.util.ArrayList;
import java.util.List;

/*
 * ============================================================
 * CLASSE CARRELLO
 * ============================================================
 *
 * Rappresenta il carrello di un cliente.
 *
 * Il carrello:
 * - contiene una lista di RigaCarrello
 * - gestisce quantità e rimozione dei prodotti
 *
 * Permette di:
 * - aggiungere un prodotto
 * - aumentare la quantità se il prodotto è già presente
 * - diminuire o rimuovere un prodotto
 * - calcolare il totale dell'ordine
 */
public class Carrello {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Lista delle righe presenti nel carrello.
     * Ogni riga rappresenta un prodotto con la relativa quantità.
     */
    private List<RigaCarrello> righe;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Crea un carrello vuoto.
     */
    public Carrello() {
        this.righe = new ArrayList<>();
    }

    // ====================================================
    // METODI PUBBLICI
    // ====================================================

    /**
     * Aggiunge un prodotto al carrello.
     *
     * Se il prodotto è già presente:
     * - incrementa la quantità di 1
     *
     * Se il prodotto NON è presente:
     * - crea una nuova RigaCarrello con quantità iniziale 1
     *
     * @param prodotto prodotto da aggiungere
     */
    public void aggiungiProdotto(Prodotto prodotto) {

        // Cerchiamo se il prodotto è già presente nel carrello
        for (RigaCarrello riga : righe) {
            if (riga.getProdotto().getId().equals(prodotto.getId())) {
                riga.setQuantita(riga.getQuantita() + 1);
                return;
            }
        }

        // Se il prodotto non è presente, aggiungiamo una nuova riga
        righe.add(new RigaCarrello(prodotto, 1));
    }

    /**
     * Diminuisce la quantità di un prodotto nel carrello.
     *
     * Se la quantità scende a zero:
     * - la riga viene rimossa dal carrello
     *
     * Metodo privato perché la gestione avviene
     * tramite handler o logiche superiori.
     *
     * @param prodottoId id del prodotto da diminuire
     */
    private void diminuisciProdotto(String prodottoId) {

        for (int i = 0; i < righe.size(); i++) {
            RigaCarrello riga = righe.get(i);

            if (riga.getProdotto().getId().equals(prodottoId)) {

                int nuovaQuantita = riga.getQuantita() - 1;

                if (nuovaQuantita <= 0) {
                    righe.remove(i);
                } else {
                    riga.setQuantita(nuovaQuantita);
                }

                return;
            }
        }
    }

    /**
     * Restituisce la lista delle righe del carrello.
     *
     * @return lista di RigaCarrello
     */
    public List<RigaCarrello> getRighe() {
        return righe;
    }

    /**
     * Calcola il totale del carrello.
     *
     * @return totale complessivo dei prodotti
     */
    public double getTotale() {

        double totale = 0;

        for (RigaCarrello riga : righe) {
            totale += riga.getTotale();
        }

        return totale;
    }

    /**
     * Svuota completamente il carrello.
     */
    public void svuota() {
        righe.clear();
    }

    /**
     * Metodo segnaposto per l'aggiunta di un prodotto
     * partendo dal suo ID.
     *
     * L'implementazione verrà completata a livello
     * di handler, utilizzando il CatalogoProdotti.
     *
     * @param prodottoId id del prodotto da aggiungere
     */
    public void aggiungiProdottoById(String prodottoId) {
        // Questo metodo sarà completato dal handler con il Catalogo
    }
}
