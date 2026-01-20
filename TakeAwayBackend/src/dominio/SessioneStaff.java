package dominio;

/**
 * ============================================================
 * CLASSE SESSIONE STAFF
 * ============================================================
 *
 * Rappresenta lo stato di autenticazione dello staff.
 *
 * La sessione è gestita tramite attributi statici:
 * - finché loggato = true, lo staff è considerato autenticato
 *
 * NOTA:
 * Questa implementazione è volutamente semplice e didattica.
 * Le credenziali sono hardcoded e non persistenti.
 */
public class SessioneStaff {

    // ====================================================
    // ATTRIBUTI STATICI
    // ====================================================

    /**
     * Indica se un membro dello staff è attualmente loggato.
     */
    private static boolean loggato = false;

    /**
     * Username hardcoded dello staff.
     */
    private static final String USERNAME = "staff";

    /**
     * Password hardcoded dello staff.
     */
    private static final String PASSWORD = "1234";

    // ====================================================
    // METODI DI AUTENTICAZIONE
    // ====================================================

    /**
     * Verifica le credenziali di accesso.
     *
     * Se le credenziali sono corrette:
     * - attiva la sessione staff
     * - imposta loggato a true
     *
     * @param user username inserito
     * @param pass password inserita
     * @return true se login riuscito, false altrimenti
     */
    public static boolean login(String user, String pass) {

        if (USERNAME.equals(user) && PASSWORD.equals(pass)) {
            loggato = true;
            return true;
        }

        return false;
    }

    /**
     * Controlla se lo staff è attualmente loggato.
     *
     * @return true se lo staff è autenticato
     */
    public static boolean isLoggato() {
        return loggato;
    }

    /**
     * Esegue il logout dello staff.
     *
     * Imposta lo stato di autenticazione a false.
     */
    public static void logout() {
        loggato = false;
    }
}
