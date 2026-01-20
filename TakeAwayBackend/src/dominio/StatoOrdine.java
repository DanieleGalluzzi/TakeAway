package dominio;

/*
 * ============================================================
 * ENUM STATO ORDINE
 * ============================================================
 *
 * Rappresenta lo stato in cui può trovarsi un ordine
 * durante il suo ciclo di vita.
 *
 * Gli stati seguono un flusso sequenziale:
 * RICEVUTO -> IN_PREPARAZIONE -> PRONTO -> CONSEGNATO
 *
 * L'enum contiene anche la logica di transizione
 * tra uno stato e il successivo.
 */
public enum StatoOrdine {

    RICEVUTO,
    IN_PREPARAZIONE,
    PRONTO,
    CONSEGNATO;

    /**
     * Restituisce il prossimo stato valido
     * in base allo stato corrente.
     *
     * @return stato successivo oppure null se non esiste
     */
    public StatoOrdine getProssimoStato() {

        switch (this) {
            case RICEVUTO:
                return IN_PREPARAZIONE;

            case IN_PREPARAZIONE:
                return PRONTO;

            case PRONTO:
                return CONSEGNATO;

            default:
                // CONSEGNATO non ha stati successivi
                return null;
        }
    }

    /**
     * Verifica se è possibile passare allo stato indicato.
     *
     * La transizione è valida solo se:
     * - il nuovo stato non è null
     * - il nuovo stato è esattamente il successivo
     *
     * @param nuovoStato stato verso cui si vuole passare
     * @return true se la transizione è consentita
     */
    public boolean puoPassareA(StatoOrdine nuovoStato) {
        return nuovoStato != null && nuovoStato.equals(getProssimoStato());
    }
}
