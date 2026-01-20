 CREATE DATABASE takeaway_express
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
 USE takeaway_express;
 
 CREATE TABLE prodotto (
    id VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descrizione TEXT,
    prezzo DECIMAL(10,2) NOT NULL CHECK (prezzo >= 0),
    categoria VARCHAR(50) NOT NULL,
    immagine LONGBLOB
);

 
 CREATE TABLE ordine (
 id INT AUTO_INCREMENT PRIMARY KEY,
 codice VARCHAR(30) NOT NULL UNIQUE,
 nome_cliente VARCHAR(100) NOT NULL,
 contatto VARCHAR(100) NOT NULL,
 note TEXT,
 totale DECIMAL(10,2) NOT NULL CHECK (totale >= 0),
 stato VARCHAR(30) NOT NULL,
 data_creazione DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 data_aggiornamento DATETIME NULL
 );
 
 CREATE TABLE riga_ordine (
 id INT AUTO_INCREMENT PRIMARY KEY,
 ordine_id INT NOT NULL,
 prodotto_id VARCHAR(20) NOT NULL,
 quantita INT NOT NULL CHECK (quantita > 0),
 prezzo_unitario DECIMAL(10,2) NOT NULL CHECK (prezzo_unitario >= 0),
 
 CONSTRAINT fk_riga_ordine_ordine
 FOREIGN KEY (ordine_id)
 REFERENCES ordine(id)
 ON DELETE CASCADE,
 
 CONSTRAINT fk_riga_ordine_prodotto
FOREIGN KEY (prodotto_id)
REFERENCES prodotto(id)	
 );

INSERT INTO prodotto VALUES
('PAN1', 'Burger Classico', 'Panino con hamburger di manzo, lattuga e pomodoro', 6.50, 'Panini', LOAD_FILE('C:\Users\gallu\OneDrive\Desktop\TakeAway\img\burgerClassico.png')),
('PAN2', 'Burger al Formaggio', 'Hamburger di manzo con formaggio fuso', 7.00, 'Panini', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\burgerFormaggio.png")),
('PAN3', 'Burger al Pollo', 'Panino con pollo croccante e salsa', 6.80, 'Panini', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\burgerPollo.png")),
('PAN4', 'Burger Vegetariano', 'Panino con burger vegetale e verdure', 6.90, 'Panini', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\burgerVegeteriano.png"));

INSERT INTO prodotto VALUES
('ANT1', 'Patatine Fritte', 'Porzione di patatine croccanti', 3.00, 'Antipasti', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\patatineFritte.png")),
('ANT2', 'Anelli di Cipolla', 'Anelli di cipolla fritti', 3.50, 'Antipasti', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\anelliCipolla.png")),
('ANT3', 'Crocchette di Patate', 'Crocchette dorate di patate', 3.80, 'Antipasti', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\crocchettePatate.png")),
('ANT4', 'Bocconcini di Pollo', 'Bocconcini di pollo fritti', 4.20, 'Antipasti', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\bocconciniPollo.png"));

INSERT INTO prodotto VALUES
('BEV1', 'Cola', 'Bibita gassata 33cl', 2.50, 'Bevande', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\cola.png")),
('BEV2', 'Aranciata', 'Bibita all’arancia 33cl', 2.50, 'Bevande', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\aranciata.png")),
('BEV3', 'Acqua Naturale', 'Bottiglia 50cl', 1.50, 'Bevande', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\acquaNaturale.png")),
('BEV4', 'Acqua Frizzante', 'Bottiglia 50cl', 1.50, 'Bevande', LOAD_FILE("C:\Users\gallu\OneDrive\Desktop\TakeAway\img\acquaFrizzante.png"));

SELECT * FROM prodotto ORDER BY categoria, nome;
