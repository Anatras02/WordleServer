# WordleServer

Il progetto WordleServer è un'applicazione server-side per il gioco Wordle. Questo server gestisce l'interazione con i client, la generazione periodica di una nuova "parola del giorno" e altre funzionalità correlate.

# Configurazione

Il server utilizza un file `config.properties` per configurare vari parametri, come la porta su cui ascoltare, la dimensione del pool di thread e l'intervallo di generazione della parola del giorno.

## **Configurazioni Possibili**

### `**port**`

- **Uso**: Porta su cui il server ascolta.
- **Esempio**: **`port=3456`**

### **`poolSize`**

- **Uso**: Dimensione del pool di thread.
- **Esempio**: **`poolSize=20`**

### **`intervalInSeconds`**

- **Uso**: Intervallo per la generazione della "parola del giorno".
- **Esempio**: **`intervalInSeconds=5`**

# Protocollo

### Descrizione Generale

Il protocollo di comunicazione WordleServer definisce le interazioni tra il client e il server attraverso specifici endpoint. Ogni endpoint può avere parametri associati, che vengono trasmessi attraverso la query string.

Il prot

### Formato della Richiesta

Le richieste al server seguono il seguente formato:

```
endpoint?key_par_1=par_1,key_par_2=par_2,...
```

Dove:

- `endpoint` rappresenta l'endpoint specifico a cui si desidera accedere.
- `key_par_n` rappresenta la chiave del parametro n.
- `par_n` rappresenta il valore associato alla chiave del parametro n.

Nel caso in cui non ci siano parametri associati all'endpoint, la richiesta avrà il seguente formato:

```
endpoint?
```

### Esempi di Richieste

1. Richiesta con parametri:

```
login?username=username,password=password
```

In questo esempio, la richiesta accede all'endpoint `login` e fornisce due parametri: `username` con valore `username` e `password` con valore `password`.

1. Richiesta senza parametri:

```
play?
```

In questo esempio, la richiesta accede all'endpoint `play` senza fornire ulteriori parametri.

### Formato della Risposta

Le risposte dal server seguono il seguente formato:

```json
{
    "status": "<string>",
    "body": {
        "message": "<string, json, ...>"
    }
}
```

Dove:

- `status` rappresenta lo stato della risposta, ad esempio "success" o "error".
- `body` contiene ulteriori dettagli sulla risposta.
- `message` all'interno del `body` fornisce un messaggio specifico relativo alla risposta.

### Considerazioni Finali

È essenziale che i client rispettino il formato delle richieste come descritto in questo protocollo per garantire una corretta interazione con il server WordleServer. Qualsiasi deviazione dal formato standard può portare a risposte inaspettate o errori di comunicazione.

## Implementazione sul Server

### Classe `Request`

La classe `Request` è stata progettata per analizzare e gestire le richieste ricevute dal client. Questa classe rappresenta il cuore dell'interazione client-server, poiché ogni azione del client inizia con una richiesta.

- **Gestione degli endpoint**: La classe separa l'endpoint dalla stringa della richiesta, permettendo al server di determinare rapidamente quale azione deve essere intrapresa.
- **Parametri flessibili**: Utilizzando una mappa `HashMap`, la classe `Request` può gestire un numero variabile di parametri, rendendo il protocollo estremamente flessibile.
- **Validazione**: La classe è stata progettata per validare le richieste in ingresso, garantendo che solo le richieste ben formate vengano elaborate.

### Classe `Response`

La classe `Response` è stata progettata per rappresentare una risposta standardizzata nel protocollo WordleServer. L'obiettivo principale di questa classe è fornire una struttura uniforme per tutte le risposte inviate dal server al client.

- **Struttura uniforme**: Con un campo `status` per indicare lo stato della risposta (ad es. "success" o "error") e un campo `body` per contenere informazioni dettagliate, la classe garantisce che ogni risposta abbia una forma riconoscibile.
- **Integrazione con JSON**: La classe utilizza la libreria Gson per serializzare l'oggetto in una stringa JSON, facilitando la comunicazione tra server e client in un formato ampiamente adottato.
- **Estensibilità**: La presenza di un campo `user` permette di associare a determinate richieste un utente che potrà essere usato altrove.
    - ******************Esempio:****************** dopo il login viene associato l’utente appena creato alla richiesta che verrà poi usato successivamente nelle `View`

---

Queste classi rappresentano la fondamenta della comunicazione nel protocollo WordleServer. La scelta di separare le richieste e le risposte in classi distinte garantisce una chiara divisione delle responsabilità e una maggiore manutenibilità del codice.

---

# Connessione

Le seguenti classi gestiscono la connessione con i client e la gestione di essi

### Main.java

La classe `Main` è il punto di ingresso dell'applicazione e gestisce le seguenti funzionalità:

- **Generazione Periodica della parola**: Il server genera periodicamente una nuova "parola del giorno". L'intervallo tra le generazioni è configurabile tramite il file `config.properties`. Se la “parola del giorno” non è stata ancora generata per quel periodo, viene generata immediatamente.
- **Gestione dei Client**: Il server ascolta le connessioni dei client e, per ogni connessione, avvia un nuovo thread per gestire le richieste del client. Questo è realizzato utilizzando un pool di thread fisso, la cui dimensione è configurabile.
- **Shutdown Hook**: È presente un hook di shutdown per chiudere il scheduler quando l'applicazione si arresta, garantendo che non ci siano risorse lasciate aperte o thread in esecuzione.

Nella progettazione della classe **`Main`**, è stata presa una decisione consapevole di utilizzare un approccio basato su **`Thread`** per gestire le connessioni dei client, piuttosto che adottare un modello basato su **`Channels`**. Questa scelta non è casuale, ma è il risultato di una valutazione ponderata delle esigenze dell'applicazione e delle implicazioni architetturali. Di seguito è presentata un'analisi dettagliata di questa decisione:

**Semplicità e Direttezza**

L'uso di un pool di thread, come **`ExecutorService`**, per gestire le connessioni dei client è un approccio tradizionale e ben consolidato. Questo modello è intuitivo e diretto: ogni connessione client viene gestita da un thread separato. Questa scelta suggerisce un'enfasi sulla chiarezza del codice e sulla facilità di manutenzione.

**Scalabilità e Carico di Lavoro**

L'adozione di un modello basato su thread indica una previsione di un carico di lavoro moderato per l'applicazione. In scenari in cui il numero di connessioni simultanee è limitato, l'approccio basato su thread è adeguato e efficiente. Tuttavia, è importante notare che, sebbene i thread possano gestire un numero significativo di connessioni, potrebbero non essere ottimali per scenari ad altissima concorrenza.

**Flessibilità e Futura Evoluzione**

Sebbene i **`Channels`** offrano vantaggi in termini di I/O non bloccante e scalabilità, la loro integrazione richiede una complessità aggiuntiva. La decisione di utilizzare thread suggerisce che, al momento attuale, la chiarezza e la flessibilità del codice hanno avuto la precedenza sulla scalabilità massima. Tuttavia, ciò non preclude la possibilità di una futura migrazione o integrazione con **`Channels`** se le esigenze dell'applicazione dovessero evolversi.

**Performance e Robustezza**

L'approccio basato su thread è ottimizzato per una comunicazione fluida e stabile. Anche se i **`Channels`** possono offrire vantaggi in scenari ad alte prestazioni, la scelta dei thread indica una valutazione che privilegia la robustezza e l'affidabilità della comunicazione.

---

### Routing.java

La classe `Routing` gestisce le rotte dell'applicazione, associando le stringhe endpoint alle rispettive classi view. Questo permette di creare dinamicamente istanze delle view basate sulle richieste dei client.

**Caratteristiche Principali**:

- **Mappa delle Rotte**: La classe contiene una mappa statica che associa gli endpoint alle rispettive classi view. Ad esempio, l'endpoint "registration" è associato alla classe `RegistrationView`.
- **Creazione Dinamica delle View**: La classe fornisce un metodo per creare dinamicamente un'istanza della view associata a un determinato endpoint. Questo metodo utilizza la riflessione per istanziare la classe view appropriata.

---

### ClientHandler.java

La classe `ClientHandler` gestisce le comunicazioni individuali con i client. Ogni client connesso viene servito da un'istanza separata di questa classe.

**Caratteristiche Principali**:

- **Gestione del Socket**: Ogni istanza di `ClientHandler` gestisce un socket specifico attraverso il quale avviene la comunicazione con il client.
- **Lettura e Scrittura sul Socket**: La classe fornisce metodi per leggere e scrivere stringhe sul socket. Questi metodi gestiscono le eccezioni e assicurano che i dati siano trasmessi correttamente.
- **Elaborazione delle Richieste**: Nel metodo `run()`, la classe legge i comandi inviati dal client, li elabora utilizzando la classe `Routing` per determinare la view appropriata, e invia le risposte appropriate al client mediante un protocollo propietario

Nella progettazione della classe **`ClientHandler`**, è stata adottata una decisione consapevole di utilizzare il pacchetto **`java.io`** per gestire le operazioni di I/O sul socket. Questa scelta riflette una valutazione ponderata delle esigenze dell'applicazione e delle implicazioni architetturali. Di seguito è presentata un'analisi dettagliata di questa decisione:

**Priorità alla Chiarezza e alla Manutenibilità**

L'API **`java.io`** è intrinsecamente più semplice e diretta rispetto a **`java.nio`**. Questa scelta suggerisce un'enfasi sulla chiarezza del codice e sulla facilità di manutenzione. In contesti in cui la leggibilità e la semplicità sono essenziali, **`java.io`** emerge come una scelta preferibile.

**Valutazione del Carico di Lavoro Anticipato**

L'utilizzo di **`java.io`** indica una previsione di un carico di lavoro leggero o moderato per l'applicazione. In scenari in cui il numero di connessioni simultanee è limitato e la comunicazione sequenziale è predominante, l'approccio basato su stream bloccanti di **`java.io`** è adeguato e efficiente.

**Flessibilità e Futura Scalabilità**

Sebbene **`java.nio`** offra vantaggi in termini di I/O non bloccante e scalabilità, la sua integrazione richiede una complessità aggiuntiva. La decisione di utilizzare **`java.io`** suggerisce che, al momento attuale, la flessibilità e la chiarezza del codice hanno avuto la precedenza sulla scalabilità massima. Tuttavia, ciò non preclude la possibilità di una futura migrazione o integrazione con **`java.nio`** se le esigenze dell'applicazione dovessero evolversi.

---

# Views

### View.java

La classe astratta `View` rappresenta una componente chiave nell'architettura del WordleServer, fungendo da interfaccia tra le richieste dei client e le risposte del server.

**Panoramica**

La `View` è stata progettata per centralizzare e standardizzare la gestione delle richieste. Ogni vista specifica nel sistema estende questa classe base, ereditando le funzionalità di controllo dei permessi e di elaborazione delle richieste.

**Gestione dei Permessi**

Una caratteristica distintiva della classe `View` è la sua capacità di gestire i permessi. Prima di elaborare qualsiasi richiesta, la vista verifica se l'utente ha i permessi necessari per eseguire l'azione richiesta. Questo meccanismo garantisce che le operazioni siano eseguite in modo sicuro e solo da utenti autorizzati.

**Elaborazione Centralizzata delle Richieste**

Dopo aver verificato i permessi, la `View` si occupa dell'elaborazione della richiesta. Questo design consente alle sottoclassi di concentrarsi sulla logica specifica della vista, mentre la gestione dei permessi e l'interazione di base con il client sono gestite dalla classe madre.

**Design Pattern: Template Method**

Il design della classe **`View`** incorpora il pattern **Template Method**. Il metodo **`handle`** funge da "template", definendo la struttura generale dell'elaborazione di una richiesta, mentre il metodo astratto **`handleRequestWithPermissions`** permette alle sottoclassi di personalizzare la logica specifica di elaborazione. Questo design garantisce una struttura coerente per la gestione delle richieste, pur permettendo una flessibilità nelle implementazioni specifiche.

**Estendibilità**

Grazie alla sua natura astratta, la `View` è facilmente estendibile. Per aggiungere una nuova vista o funzionalità, basta creare una sottoclasse e implementare la logica specifica desiderata, sfruttando al contempo le funzionalità di base fornite dalla classe `View`.

---

### LoginView.java

Gestisce le richieste di login degli utenti.

La vista verifica le credenziali fornite e restituisce una risposta appropriata in base all'esito del tentativo di login.

---

### RegistrationView.java

Gestisce le richieste di registrazione degli utenti.

La vista verifica se l'username fornito è già in uso e, in caso contrario, procede con la registrazione.

---

### PlayView.java

Si occupa delle richieste di inizializazzione di gioco.

Verifica se l'utente ha già iniziato o completato una partita per il periodo corrente e gestisce di conseguenza l’inizializazzione della partita

---

### SendWord.java

Si occupa delle richieste degli utenti per inviare parole durante una partita.

Verifica se l’utente ha in corso una partita valida e non terminata, in caso affermativo la vista segnala la vittoria, la sconfitta o fornisce suggerimenti sull'esattezza delle lettere inviate in relazione alla parola da indovinare secondo le specifiche del progetto.

---

### ShareView.java

Gestisce le richieste di condivisione della partita.

Verifica se l’utente ha completato la partita e poi usa un socket multicast per condividere i dettagli della partita con altri utenti.

---

### StatisticView.java

Si occupa delle richieste di visualizzazione delle statistiche personali degli utenti.

La vista recupera e restituisce le statistiche dell'utente in formato JSON.

---

## Permessi

Come descritto sopra l’applicazione implementa un sistema di permessi facilmente estendibile per poter andare ad aggiungere dei permessi che devono essere verificati per eseguire una specifica `View`

### Permission.java

L'interfaccia **`Permission`** stabilisce un contratto per tutte le classi di permessi nel sistema WordleServer. Questo design consente di avere una varietà di permessi con implementazioni diverse, ma con una struttura comune.

- **Nome del Permesso:** Ogni implementazione deve fornire un nome univoco per il permesso attraverso **`getPermissionName()`**.
- **Logica di Verifica:** Ogni permesso ha una logica specifica di verifica, definita in **`hasPermission(User user, Game lastGame)`**, che determina se il permesso è verificato o meno.

Utilizzando questa interfaccia come base, il sistema può facilmente estendersi con nuovi permessi mantenendo coerenza e modularità nel design.

---

### IsGameFinishedPermission.java

Questo permesso verifica se una partita è stata completata.

È utilizzato per garantire che determinate operazioni siano eseguite solo dopo la conclusione di una partita.

---

### IsGameNotFinishedPermission.java

Questo permesso verifica se una partita non è stata ancora completata.

Serve per assicurarsi che determinate azioni siano eseguite solo quando una partita è ancora in corso.

---

### IsGameStartedPermission.java

Questo permesso verifica se una partita è stata iniziata.

È utilizzato per garantire che determinate operazioni siano eseguite solo dopo l'inizio di una partita.

---

### IsGameValidPermission.java

Questo permesso verifica se una partita è valida.

Serve per assicurarsi che determinate azioni siano eseguite solo quando una partita è in uno stato valido.

---

### IsUserLoggedPermission.java

Questo permesso verifica se un utente è loggato nel sistema.

È fondamentale per garantire che determinate operazioni siano eseguite solo da utenti autenticati.

---

### NoPermissionException.java

L'eccezione **`NoPermissionException`** rappresenta una situazione in cui una specifica vista tenta di eseguire un'operazione senza avere il permesso appropriato. Questa eccezione è lanciata per notificare che un'operazione non può essere eseguita a causa di restrizioni di permesso.

---

Queste classi di permessi rappresentano una parte essenziale del sistema di sicurezza e integrità di WordleServer, garantendo che le operazioni siano eseguite in modo appropriato e sicuro.

---

# **Model e ORM**

### **Model.java**

La classe astratta **`Model`** rappresenta un fondamento nell'architettura del WordleServer, fungendo da rappresentazione base per i dati e la loro persistenza.

**Panoramica**

Il **`Model`** funge da classe madre per tutte le rappresentazioni dei dati all'interno del sistema. Ogni istanza di un modello specifico eredita le funzionalità fondamentali fornite da questa classe astratta, tra cui le capacità di salvataggio e clonazione.

**Interazione con l'ORM**

Una caratteristica chiave del **`Model`** è la sua stretta relazione con l'Object-Relational Mapping (ORM). La funzione di questa relazione è di mappare i dati dell'oggetto ai dati persistenti. Il metodo **`getOrm()`** consente a ciascun modello specifico di ottenere l'ORM associato, garantendo che la persistenza dei dati sia gestita in modo appropriato.

**Salvataggio dei Dati**

La funzione **`salva()`** utilizza l'ORM associato per persistere i dati del modello. Questo metodo centralizza la logica di salvataggio, assicurandosi che ogni modello specifico possa essere salvato seguendo un protocollo standard.

**Clonazione del Modello**

La clonazione è un meccanismo essenziale in molti scenari, consentendo la creazione di copie indipendenti di un oggetto. Il **`Model`** fornisce un metodo di clonazione che restituisce una copia esatta dell'oggetto. Le sottoclassi specifiche possono estendere o personalizzare questo comportamento se necessario.

### **Orm.java**

La classe astratta **`Orm`** rappresenta un componente fondamentale all'interno del WordleServer, fornendo un meccanismo di Object-Relational Mapping (ORM) per la manipolazione dei dati.

**Panoramica**

La **`Orm`** è stata progettata per fornire funzionalità di base per la gestione dei dati, come il caricamento e il salvataggio dei dati su file. Essendo una classe astratta, serve come base per classi ORM specializzate che gestiscono modelli di dati specifici.

**Interazione con File JSON**

La classe **`Orm`** utilizza la libreria Gson per caricare e salvare i dati in un file JSON. Questo permette una facile persistenza e recupero dei dati, garantendo al contempo una rappresentazione leggibile e standardizzata dei dati.

**Salvataggio Asincrono e Ottimizzato**

Una delle caratteristiche distintive della classe **`Orm`** è l'uso della classe **`LastTaskExecutor`** per gestire il salvataggio asincrono dei dati. Questo design garantisce che, in caso di molteplici richieste di salvataggio in rapida successione, solo l'ultimo task venga effettivamente eseguito. Questo assicura che solo i dati più recenti vengano salvati, ottimizzando le operazioni di I/O e garantendo la coerenza dei dati.

**Gestione delle Risorse**

La classe **`Orm`** implementa l'interfaccia **`AutoCloseable`**, permettendo una gestione efficiente delle risorse. Questo garantisce che le risorse, come i thread associati all'executor, vengano rilasciate correttamente quando l'ORM non è più necessario.

**Thread Safety**

La thread safety in **`Orm`** è garantita attraverso l'uso di metodi sincronizzati e strutture dati thread-safe. Ad esempio, il metodo **`salvaSuFile`** è sincronizzato, assicurando che solo un thread alla volta possa accedere alla risorsa di salvataggio. Inoltre, l'uso di **`LastTaskExecutor`** garantisce che le operazioni asincrone siano gestite in modo thread-safe, evitando potenziali condizioni di gara. Questo design consente a **`Orm`** di operare in modo efficiente in ambienti multithread senza compromettere l'integrità dei dati.

---

## User

### **User.java**

La classe **`User`** rappresenta un'entità fondamentale all'interno del WordleServer, fornendo una rappresentazione dettagliata di un utente e delle sue interazioni con il sistema.

**Panoramica**

La **`User`** è stata progettata per centralizzare e standardizzare la gestione delle informazioni relative agli utenti. Ogni utente nel sistema è rappresentato da un'istanza di questa classe, che contiene dettagli come l'username, la password e le sessioni di gioco associate.

**Gestione delle Sessioni di Gioco**

Utilizza una **`PriorityBlockingQueue`** per mantenere e ordinare le sessioni di gioco, garantendo un accesso efficiente all'ultima sessione. Questa coda è thread-safe, garantendo che le operazioni su di essa siano atomiche e non causino condizioni di gara.

**Autenticazione e Registrazione**

La classe **`User`** fornisce metodi statici per la registrazione e l'autenticazione degli utenti. Questi metodi interagiscono con la classe **`UserOrm`** per verificare le credenziali e salvare o recuperare le informazioni degli utenti.

**Thread Safety**

La classe **`User`** è stata progettata per essere thread-safe.

Sebbene la thread safety potrebbe non essere essenziale per l'attuale implementazione del programma, è stata progettata in previsione di possibili sviluppi futuri. In scenari in cui si potrebbe desiderare di delegare alcune operazioni a thread separati, avere una classe già thread-safe renderebbe lo sviluppo molto più semplice e immediato.

**Conversione in JSON**

La classe **`User`** fornisce un metodo per convertire l'oggetto in formato JSON, utilizzando la libreria Gson. Questo è particolarmente utile per l'interazione con il client.

### **UserOrm.java**

La classe **`UserOrm`** rappresenta un componente essenziale all'interno del WordleServer, fornendo un meccanismo di Object-Relational Mapping (ORM) per la gestione degli utenti.

**Panoramica**

La **`UserOrm`** è stata progettata per centralizzare e standardizzare la gestione delle informazioni relative agli utenti. Questa classe fornisce funzionalità per caricare, salvare, aggiungere e recuperare informazioni sugli utenti, utilizzando una mappa concorrente per garantire l'accesso efficiente e la thread-safety.

**Gestione Concorrente**

Una delle principali caratteristiche della classe **`UserOrm`** è l'uso di una **`ConcurrentHashMap`** per memorizzare gli utenti. Questa scelta garantisce un accesso efficiente in O(1) e la thread-safety durante le operazioni di lettura e scrittura.

**Design Pattern: Singleton**

Il design della classe **`UserOrm`** incorpora il pattern **Singleton**. Questo garantisce un punto di accesso centralizzato ai dati degli utenti e assicura coerenza e integrità.

**Interazione con File JSON**

La classe **`UserOrm`** utilizza la libreria Gson per caricare e salvare gli utenti in un file JSON. Questo permette una facile persistenza e recupero dei dati, garantendo al contempo una rappresentazione leggibile e standardizzata.

**Estendibilità**

La struttura modulare e l'uso di metodi sincronizzati rendono la classe **`UserOrm`** facilmente estendibile. Nuove funzionalità o modifiche possono essere integrate con minimo impatto sul codice esistente.

---

## DailyWord

### **DailyWord.java**

La classe **`DailyWord`** rappresenta una componente essenziale all'interno del WordleServer, fornendo una rappresentazione della "parola del giorno" associata a un timestamp.

**Panoramica**

Nonostante il nome "DailyWord", questa classe non rappresenta necessariamente una parola che viene generata ogni giorno. La frequenza di generazione della parola del giorno è configurabile e può variare. Per ulteriori dettagli sulla frequenza di generazione e sulla logica associata, si consiglia di consultare la descrizione della classe **`Main.java`**.

**Gestione del Timestamp**

Ogni istanza di **`DailyWord`** è associata a un timestamp, che rappresenta il momento in cui la parola è stata generata o impostata. Questo permette di tracciare la cronologia delle parole generate e di determinare la "parola del giorno" corrente.

**Interazione con l'ORM**

La classe **`DailyWord`** interagisce strettamente con **`DailyWordOrm`**, un Object-Relational Mapping (ORM) specifico per la gestione delle parole del giorno. Questo ORM fornisce funzionalità per caricare, salvare e aggiornare le parole nel sistema.

### **DailyWordOrm.java**

La classe **`DailyWordOrm`** rappresenta un Object-Relational Mapping (ORM) specifico per la gestione della "parola del giorno" all'interno del WordleServer.

**Panoramica**

**`DailyWordOrm`** fornisce funzionalità per caricare, salvare, aggiornare e generare parole giornaliere. Questa classe è essenziale per garantire che la parola del giorno sia coerente e accessibile in tutto il sistema.

**Singleton Pattern**

La classe **`DailyWordOrm`** utilizza il pattern Singleton per garantire che esista una sola istanza di questa classe in tutto il sistema. Questo assicura che tutte le operazioni relative alla "parola del giorno" siano centralizzate e consistenti.

**Thread Safety**

La thread safety in **`DailyWordOrm`** è garantita attraverso l'uso di metodi sincronizzati. Questo assicura che, anche in un ambiente multithread, le operazioni di lettura e scrittura sul file e sulla parola del giorno corrente siano atomiche e non causino condizioni di gara.

**Gestione della Parola del Giorno**

La classe fornisce metodi per recuperare la parola del giorno corrente, aggiornarla e generarne una nuova. La parola del giorno viene salvata in un file JSON, e la classe si occupa di caricare e salvare questi dati in modo efficiente e sicuro.

**Verifica della Parola**

**`DailyWordOrm`** contiene un metodo per verificare se una parola specifica è presente in un file di parole. Questo può essere utile per verificare se una parola inviata dal client è presente nella lista delle parole o meno.

**Generazione di Parole Casuali**

Una delle funzionalità chiave di **`DailyWordOrm`** è la capacità di generare una nuova parola del giorno in modo casuale. Questo viene fatto leggendo un file di parole e selezionando una riga casuale. Per garantire l'efficienza di memoria, il file viene letto due volte: la prima volta per contare il numero di righe e la seconda volta per recuperare la parola alla riga casuale selezionata.

Questo approccio è particolarmente vantaggioso quando si considera che il file delle parole potrebbe contenere un numero molto elevato di voci. Caricare tutte queste parole in memoria potrebbe non solo essere inefficiente in termini di spazio, ma potrebbe anche rallentare altre operazioni nel sistema a causa dell'uso eccessivo di risorse.

Inoltre, la generazione di una nuova parola del giorno non è un'operazione che viene eseguita frequentemente, ma piuttosto in momenti specifici. Di conseguenza, è più sensato ottimizzare per l'uso della memoria piuttosto che per la velocità.

Infine, è importante notare che questa operazione viene eseguita su un thread separato dedicato esclusivamente a questo compito. Questo garantisce che, anche se la lettura del file avviene due volte, l'impatto sulle prestazioni generali del sistema è minimo e non influisce sulle altre operazioni in corso.

In sintesi, l'approccio adottato da **`DailyWordOrm`** per generare parole casuali è un equilibrio tra efficienza di memoria e prestazioni, garantendo che il sistema rimanga reattivo e scalabile anche quando si lavora con grandi set di dati.

---

## Game

### **Game.java**

La classe **`Game`** rappresenta una singola sessione di gioco con le relative metriche. Questa classe è fondamentale per tracciare il progresso di un giocatore durante una partita e per conservare le statistiche di gioco.

**Thread Safety**

La classe **`Game`** è stata progettata per essere thread-safe. Utilizza strutture dati atomiche come **`AtomicBoolean`** e **`AtomicInteger`** per garantire operazioni atomiche su variabili.

Inoltre, la variabile **`parole`** utilizza un **`ArrayList`** sincronizzato tramite il metodo **`Collections.synchronizedList`** per garantire la thread safety nelle operazioni di accesso e modifica della lista.

È importante notare che l'utilizzo di strutture dati sincronizzate come **`ArrayList`** può comportare problemi di prestazioni in scenari ad alta concorrenza. Tuttavia visto che questa classe andrà usata in ambienti di bassa concorrenza l'approccio con **`ArrayList`** sincronizzato può essere adeguato per le esigenze attuali.

Sebbene la thread safety potrebbe non essere essenziale per l'attuale implementazione del programma, è stata progettata in previsione di possibili sviluppi futuri. In scenari in cui si potrebbe desiderare di delegare alcune operazioni a thread separati, avere una classe già thread-safe renderebbe lo sviluppo molto più semplice e immediato.

**Comparazione tra Partite**

La classe **`Game`** implementa l'interfaccia **`Comparable<Game>`**, permettendo di confrontare due partite in base ai loro timestamp. Questo è particolarmente utile per ordinare le partite in base al momento in cui sono state giocate.

---

## **Statistica**

### **Statistica.java**

La classe **`Statistica`** rappresenta un aggregatore di metriche di gioco, fornendo un riepilogo statistico delle sessioni di gioco completate. Questa classe è essenziale per fornire una visione d'insieme delle performance di un giocatore nel corso del tempo.

**Panoramica**

La **`Statistica`** è stata progettata per aggregare e calcolare le metriche dai giochi completati. Ogni metrica, come il numero di partite giocate, il numero di partite vinte, la serie di vittorie e la distribuzione dei tentativi, è mantenuta e aggiornata in modo thread-safe.

**Thread Safety**

La classe **`Statistica`** è stata progettata per essere thread-safe. Utilizza strutture dati concorrenti e atomiche, come **`AtomicInteger`** e **`ConcurrentHashMap`**, per garantire operazioni atomiche e thread-safe. Questo significa che le metriche possono essere aggiornate da più thread contemporaneamente senza causare condizioni di gara o inconsistenze nei dati. Inoltre, il metodo **`calcola`** è sincronizzato, garantendo che le operazioni che modificano lo stato interno della classe siano eseguite in modo sequenziale, evitando potenziali problemi.

Sebbene la thread safety potrebbe non essere essenziale per l'attuale implementazione del programma, è stata progettata in previsione di possibili sviluppi futuri. Un esempio potrebbe essere quello di gestire il calcolo delle statistiche da parte di thread separati.

---

## **Utils**

### **HashUtil.java**

La classe **`HashUtil`** fornisce metodi per l'hashing delle stringhe utilizzando l'algoritmo SHA-256. Tuttavia, potrebbe essere ulteriormente migliorata utilizzando un design pattern che consenta di selezionare dinamicamente l'algoritmo di crittografia. Ad esempio, potrebbe essere implementato il design pattern Strategy, che permette di definire una famiglia di algoritmi, incapsularli e renderli intercambiabili.

È importante sottolineare che, quando si cambia l'algoritmo di hashing, è necessario prestare attenzione alle password già salvate nel file. Poiché i nuovi algoritmi potrebbero generare hash diversi per le stesse password, sarebbe fondamentale adottare una strategia per migrare in modo sicuro le password esistenti al nuovo algoritmo. Questo processo potrebbe richiedere una pianificazione accurata e potrebbe coinvolgere l'aggiornamento graduale delle password durante il login degli utenti.

---

### **LastTaskExecutor.java**

**`LastTaskExecutor`** è un'implementazione personalizzata dell'interfaccia **`Executor`** che assicura che solo l'ultimo task inviato venga eseguito. Quando un task è già in esecuzione e vengono inviati nuovi task, i task intermedi vengono scartati e sostituiti dall'ultimo task arrivato. In altre parole, solo il task più recente nella coda verrà effettivamente eseguito, mentre i task precedenti saranno ignorati e scartati.

---

### **LocalDateTimeAdapter.java**

**`LocalDateTimeAdapter`** è un adapter GSON per serializzare e deserializzare oggetti **`LocalDateTime`**. Questo adapter utilizza un formato specifico ("yyyy-MM-dd HH:mm:ss") per la conversione tra stringhe e oggetti **`LocalDateTime`**.

---

### **MulticastSocketUtil.java**

La classe **`MulticastSocketUtil`** fornisce metodi per l'invio di messaggi multicast.

---

### **ConfigHandler.java**

La classe **`ConfigHandler`** rappresenta un gestore di configurazioni, permettendo di caricare e recuperare proprietà da un file specificato in modo asincrono. Questa classe è stata progettata per migliorare l'efficienza, caricando le proprietà in un thread separato, permettendo all'applicazione principale di continuare con altre operazioni.

**Panoramica**

La **`ConfigHandler`** è stata progettata per facilitare l'interazione con i file di configurazione in modo asincrono. Una volta creato un oggetto **`ConfigHandler`**, inizia immediatamente a caricare le proprietà in un thread separato. Questo significa che l'applicazione principale non deve attendere il completamento del caricamento e può procedere con altre operazioni.

**Sincronizzazione e Thread Safety**

La classe utilizza meccanismi di sincronizzazione per garantire che le proprietà siano caricate correttamente prima di essere accessibili. Se un thread tenta di accedere alle proprietà mentre sono ancora in fase di caricamento, il thread attende finché il caricamento non è completo.

**Lazy Loading**

Durante la fase di progettazione, è stata considerata l'implementazione del "lazy loading" per la classe **`ConfigHandler`**. Tuttavia, questa idea è stata successivamente scartata per i seguenti motivi:

1. **Utilizzo completo delle proprietà:** Nell'uso tipico, tutte le proprietà vengono effettivamente utilizzate poco dopo la creazione dell'istanza di **`ConfigHandler`**. Pertanto, il ritardo nell'accesso ai dati non avrebbe offerto benefici significativi.
2. **Dimensione limitata del file di configurazione:** I file di configurazione gestiti sono generalmente piccoli e contengono un numero limitato di proprietà. Di conseguenza, l'overhead di memoria associato al caricamento anticipato delle proprietà è trascurabile.

Data la natura e l'utilizzo previsto della classe, si è deciso che un approccio di caricamento anticipato era più appropriato e efficiente per le esigenze del progetto.