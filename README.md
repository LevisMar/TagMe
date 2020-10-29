# TagMe

## Breve descrizione
Questo progetto è stato creato durante lo svolgimento della tesi di laurea in Informatica, e consente di gestire una libreria, la quale può essere pubblica o privata, dove i       libri possono essere prestati ad utenti esterni al sistema, ed in seguito restituiti da quest'ultimi. Ogni libro verrà registrato all'interno del database, e per evitare di dover defnire e creare un codice per i prodotti, come codice univoco verrà utilizzato l'ISBN del libro, ovvero una sequenza numerica di 13 cifre usata internazionalmente per la classifcazione dei libri, che sarà il suo barcode.

## Applicazione web
La parte web viene utilizzata dagli utenti autorizzati (proprietari del sistema), i quali potranno gestire i libri presenti all'interno del database, comprese le informazioni quali autori e editori, ed aggiungere eventualmente altri libri, autori, editori e gli utenti che andranno ad interagire con la libreria prendendo in prestito e restituendo i libri.

## Applicazione mobile (Android)
L'app mobile verrà utilizzata da un membro dello sta per registrare le transazioni, che siano prestiti o restituzioni, sui libri da parte di un determinato utente. L'app catturerà il barcode del libro (ISBN), che verrà poi usato per cercare in modo automatico il libro all'interno del sistema.
