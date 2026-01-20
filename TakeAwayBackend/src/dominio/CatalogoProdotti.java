package dominio;

import database.GestoreDatabase;
import database.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/*
 * ============================================================
 * CLASSE CATALOGO PRODOTTI
 * ============================================================
 *
 * Questa classe si occupa della comunicazione con il database
 * per il recupero dei prodotti disponibili nel menu.
 *
 * Il suo compito è:
 * - eseguire le query SQL
 * - leggere i dati dal database
 * - trasformare ogni record in un oggetto Prodotto
 *
 * Rappresenta quindi il ponte tra database e dominio.
 */
public class CatalogoProdotti {

    // ====================================================
    // ATTRIBUTI
    // ====================================================

    /**
     * Gestore delle connessioni al database.
     * Viene fornito dall'esterno (dependency injection).
     */
    private GestoreDatabase gestoreDatabase;

    // ====================================================
    // COSTRUTTORE
    // ====================================================

    /**
     * Costruttore del catalogo prodotti.
     *
     * @param gestoreDatabase gestore delle connessioni al database
     */
    public CatalogoProdotti(GestoreDatabase gestoreDatabase) {
        this.gestoreDatabase = gestoreDatabase;
    }

    // ====================================================
    // METODI PUBBLICI
    // ====================================================

    /**
     * Recupera tutti i prodotti dal database e li
     * converte in una lista di oggetti {@link Prodotto}.
     *
     * Per ogni prodotto:
     * - legge i dati testuali
     * - legge l'immagine come byte[]
     * - converte l'immagine in Base64 per l'uso HTML
     *
     * @return lista di prodotti disponibili nel catalogo
     */
    public List<Prodotto> getTuttiProdotti() {

        List<Prodotto> prodotti = new ArrayList<>();

        try {

            // Apertura connessione al database
            Connection conn = gestoreDatabase.getConnessione();

            // Preparazione ed esecuzione query
            PreparedStatement ps = conn.prepareStatement(Query.SELECT_TUTTI_PRODOTTI);
            ResultSet rs = ps.executeQuery();

            // Creazione di un oggetto Prodotto per ogni riga
            while (rs.next()) {

                String id = rs.getString("id");
                String nome = rs.getString("nome");
                String descrizione = rs.getString("descrizione");
                double prezzo = rs.getDouble("prezzo");
                String categoria = rs.getString("categoria");
                byte[] img = rs.getBytes("immagine");

                // Controllo presenza immagine
                if (img == null || img.length == 0) {
                    System.out.println("ID: " + id + " | IMG bytes: " + img.length);
                    continue;
                }

                // Conversione immagine in Base64
                String base64 = Base64.getEncoder().encodeToString(img);

                // Creazione oggetto dominio
                Prodotto prodotto = new Prodotto(
                        id,
                        nome,
                        descrizione,
                        prezzo,
                        categoria,
                        base64
                );

                prodotti.add(prodotto);
            }

        } catch (SQLException e) {
            System.err.println("Errore nel caricamento dei prodotti");
            e.printStackTrace();
        }

        return prodotti;
    }
}
