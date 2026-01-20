package app;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import server.*;

import database.GestoreDatabase;

import dominio.CatalogoProdotti;
import dominio.GestoreOrdini;
import dominio.Carrello;

/*
 * ============================================================
 * CLASSE DI AVVIO DEL SERVER HTTP
 * ============================================================
 *
 * Questa classe rappresenta il punto di ingresso
 * dell'intera applicazione Takeaway.
 *
 * Qui vengono eseguite tutte le operazioni di inizializzazione:
 * - avvio del server HTTP
 * - creazione della connessione al database
 * - istanziazione delle classi di dominio principali
 * - registrazione degli HttpHandler (rotte)
 *
 * ------------------------------------------------------------
 * NOTA SULL'USO DI "throws Exception"
 * ------------------------------------------------------------
 * Nel metodo main() viene usato "throws Exception" invece di
 * "throws IOException" perché in questa fase l'applicazione
 * sta eseguendo operazioni di inizializzazione critiche:
 *
 * - avvio del server HTTP
 * - apertura della connessione al database
 * - creazione delle istanze principali
 * - registrazione delle rotte
 *
 * Gli errori che possono verificarsi non sono solo di tipo I/O,
 * ma includono anche SQLException, errori di configurazione
 * e altre eccezioni di runtime.
 *
 * Utilizzando "throws Exception" permettiamo a qualunque
 * problema di emergere immediatamente e bloccare l'avvio
 * dell'applicazione, evitando di avviare un sistema
 * in uno stato inconsistente.
 *
 * Negli HttpHandler, invece, viene usato "throws IOException"
 * perché lì si gestiscono solo operazioni di rete e stream,
 * e un errore su una singola richiesta non deve causare
 * l'arresto dell'intero server.
 */
public class AvvioServer {

    /**
     * Metodo main: punto di avvio dell'applicazione.
     *
     * @param args argomenti da linea di comando (non utilizzati)
     * @throws Exception intercetta qualsiasi errore critico
     *                   durante la fase di avvio
     */
    public static void main(String[] args) throws Exception {

        // ====================================================
        // CONFIGURAZIONE E AVVIO SERVER HTTP
        // ====================================================

        // Creazione del server HTTP sulla porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        System.out.println("Server avviato sulla porta 8080");

        // ====================================================
        // INIZIALIZZAZIONE COMPONENTI PRINCIPALI
        // ====================================================

        // Gestore della connessione e delle operazioni sul database
        GestoreDatabase gestoreDatabase = new GestoreDatabase();

        // Catalogo dei prodotti disponibili nel menu
        CatalogoProdotti catalogoProdotti = new CatalogoProdotti(gestoreDatabase);

        // Gestore degli ordini (creazione, salvataggio, stato)
        GestoreOrdini gestoreOrdini = new GestoreOrdini(gestoreDatabase);

        // Carrello condiviso per la sessione utente
        Carrello carrello = new Carrello();

        // ====================================================
        // REGISTRAZIONE DEGLI HANDLER (ROTTE)
        // ====================================================

        // Pagina principale con menu prodotti
        server.createContext("/", new MenuHandler(catalogoProdotti, carrello));

        // Gestione del carrello (aggiunta, rimozione, visualizzazione)
        server.createContext("/carrello", new CarrelloHandler(carrello, catalogoProdotti));

        // Creazione e invio dell'ordine
        server.createContext("/ordine", new OrdineHandler(gestoreOrdini, carrello));

        // Login area staff
        server.createContext("/staff/login", new LoginStaffHandler());

        // Dashboard staff per la gestione degli ordini
        server.createContext("/staff/dashboard", new DashboardStaffHandler(gestoreOrdini));

        // Servizio per il recupero delle immagini dal database
        server.createContext("/img", new ImageHandler(gestoreDatabase));

        // Verifica stato ordine da parte del cliente
        server.createContext("/ordine/stato", new StatoOrdineClienteHandler(gestoreOrdini));

        // ====================================================
        // AVVIO SERVER
        // ====================================================

        server.start();
    }
}
