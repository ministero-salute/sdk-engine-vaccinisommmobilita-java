# **1.Introduzione**

## ***1.1 Obiettivi del documento***

Il Ministero della Salute (MdS) metterà a disposizione degli Enti, da cui riceve dati, applicazioni SDK specifiche per flusso logico e tencnologie applicative (Java, PHP e C#) per verifica preventiva (in casa Ente) della qualità del dato prodotto.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.002.png)

Nel presente documento sono fornite la struttura e la sintassi dei tracciati previsti dalla soluzione SDK per avviare il proprio processo elaborativo e i controlli di merito sulla qualità, completezza e coerenza dei dati.

Gli obiettivi del documento sono:

- fornire una descrizione funzionale chiara e consistente dei tracciati di input a SDK;
- fornire le regole funzionali per la verifica di qualità, completezza e coerenza dei dati.

In generale, la soluzione SDK è costituita da 2 diversi moduli applicativi (Access Layer e Validation Engine) per abilitare

- l’interoperabilità con il contesto tecnologico dell’Ente in cui la soluzione sarà installata;
- la validazione del dato ed il suo successivo invio verso il MdS.

La figura che segue descrive la soluzione funzionale ed i relativi benefici attesi.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.003.png)

## ***1.2 Acronimi***

Nella tabella riportata di seguito sono elencati tutti gli acronimi e le definizioni adottati nel presente documento.


|**#**|**Acronimo / Riferimento**|**Definizione**|
| - | - | - |
|1|NSIS|Nuovo Sistema Informativo Sanitario|
|2|SDK|Software Development Kit|
|3|AVN|Anagrafe Nazionale Vaccini|
|4|SSN|Sistema Sanitario Nazionale|
|5|CI|Codice Identificativo|
|6|AIC|Autorizzazione alla Immissione in Commercio del vaccino in Italia rilasciata dall’Agenzia Italiana del Farmaco|


# **2. Architettura SDK**

## ***2.1 Architettura funzionale***

Di seguito una rappresentazione architetturale del processo di gestione e trasferimento dei flussi dall’ente verso l’area MdS attraverso l’utilizzo dell’applicativo SDK e il corrispondente diagramma di sequenza.

![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.004.jpeg)


1. L’utente dell’ente caricherà in una apposita directory (es. /sdk/input/) il flusso sorgente.  L’utente avvierà l’SDK passando in input una serie di parametri descritti in dettaglio al paragrafo 3.1
1. La compenente Access Layer estrae dalla chiamata dell’ente i parametri utilizzati per lanciare l’SDK,  genera un identificativo ID\_RUN, e un file chiamato “{ID\_RUN}.json” in cui memorizza le informazioni dell’esecuzione.
1. I record del flusso verranno sottoposti alle logiche di validazione e controllo definite nel Validation Engine. Nel processare il dato, il Validation Engine acquisirà da MdS eventuali anagrafiche di validazione del dato stesso.
1. Generazione del file degli scarti contenente tutti i record in scarto con evidenza degli errori riscontrati. I file di scarto saranno memorizzati in cartelle ad hoc (es. /sdk/esiti).
1. Tutti i record che passeranno i controlli verranno inseriti in un file xml copiato in apposita cartella (es /sdk/xml\_output), il quale verrà eventualmente trasferito a MdS utilizzando la procedura “invioFlussi” esposta da GAF WS (tramite PDI). A fronte di un’acquisizione, il MdS fornirà a SDK un identificativo (ID\_UPLOAD) che sarà usato da SDK sia per fini di logging che di recupero del File Unico degli Scarti.
1. A conclusione del processo di verifica dei flussi, il Validation Engine eseguirà le seguenti azioni:

 a. Aggiornamento file contenente il riepilogo dell’esito dell’elaborazione del Validation Engine e del ritorno dell’esito da parte di MdS. I file contenenti l’esito dell’elaborazione saranno memorizzati in cartelle ad hoc (es. /sdk/run).

 b. Consolidamento del file di log applicativo dell’elaborazione dell’SDK in cui sono disponibili una serie di informazioni tecniche (Es. StackTrace di eventuali errori).

 c. Copia del file generato al punto 5, se correttamente inviato al MdS, in apposita cartella (es. /sdk/sent).

Ad ogni step del precedente elenco e a partire dal punto 2, l’SDK aggiornerà di volta in volta il file contenente l’esito dell’elaborazione.

**Nota: l’SDK elaborerà un solo file di input per esecuzione.**

In generale, le classi di controllo previste su Validation Engine sono:

- Controlli FORMALI (es. correttezza dei formati e struttura record)
- Controlli SINTATTICI (es. check correttezza del Codice Fiscale)
- Controlli di OBBLIGATORIETÀ DEL DATO (es. Codice Prestazione su flusso…)
- Controlli STRUTTURE FILE (es. header/footer ove applicabile)
- Controlli di COERENZA CROSS RECORD
- Controlli di corrispondenza dei dati trasmessi con le anagrafiche di riferimento
- Controlli di esistenza di chiavi duplicate nel file trasmesso rispetto alle chiavi logiche individuate per ogni tracciato.

Si sottolinea che la soluzione SDK non implementa controlli che prevedono la congruità del dato input con la base date storica (es: non viene verificato se per un nuovo inserimento (Tipo = I) la chiave del record non sia già presente sulla struttura dati MdS).

## ***2.2 Architettura di integrazione***

La figura sottostante mostra l’architettura di integrazione della soluzione SDK con il MdS. Si evidenzia in particolare che:

- Tutti i dati scambiati fra SDK e MdS saranno veicolati tramite Porta di Interoperabilità (PDI);
- Il MdS esporrà servizi (API) per il download di dati anagrafici;
- SDK provvederà ad inviare vs MdS l’output (record validati) delle proprie elaborazioni. A fronte di tale invio, il MdS provvederà a generare un identificativo di avvenuta acquisizione del dato (ID\_UPLOAD) che SDK memorizzerà a fini di logging.


![](img/Aspose.Words.9913eba7-47c2-4b0d-8965-0d60acca337b.006.png)

# **3. Funzionamento della soluzione SDK**

In questa sezione è descritta le specifica di funzionamento del flusso **VSM**  per l’alimentazione dello stesso


## ***3.1 Input SDK***

In fase di caricamento del file verrano impostati i seguenti parametri che andranno in input al SDK in fase di processamento del file:


|**NOME PARAMETRO**|**DESCRIZIONE**|**LUNGHEZZA**|**DOMINIO VALORI**|
| :- | :- | :- | :- |
|ID CLIENT|Identificativo univoco della trasazione che fa richiesta all'SDK|100|Non definito|
|NOME FILE INPUT|Nome del file per il quale si richiede il processamento lato SDK|256|Non definito|
|ANNO RIFERIMENTO|Stringa numerica rappresentante l’anno di riferimento per cui si intende inviare la fornitura|4|Anno (Es. 2022)|
|TIPO TRASMISSIONE |Indica se la trasmissione dei dati verso MDS avverrà in modalità full (F) o record per record (R). Per questo flusso la valorizzazione del parametro sarà impostata di default a F|1|F/R|
|FINALITA' ELABORAZIONE|Indica se i flussi in output prodotti dal SDK verranno inviati verso MDS (Produzione) oppure se rimarranno all’interno del SDK e il processamento vale solo come test del flusso (Test)|1|Produzione/Test|
|CODICE REGIONE|<p>Individua la Regione a cui afferisce la struttura. Il codice da utilizzare è quello a tre caratteri definito con DM 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n.240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.</p><p></p>|3|Es. 010|

Inoltre è previsto anche il parametro Periodo Riferimento, i cui valori differiscono in base al flusso specifico.
Di seguito la tabella specifica per il flusso VSM:



|**NOME PARAMETRO**|**DESCRIZIONE**|**LUNGHEZZA**|**DOMINIO VALORI**|
| :- | :- | :- | :- |
|PERIODO RIFERIMENTO|Stringa alfanumerica rappresentante il periodo per il quale si intende inviare la fornitura|2|**13** (rappresenta un alias per il quale MDS usa come data di competenza l’anno e il mese di riferimento del record piuttosto che il parametro periodo di riferimento passato in input alla procedura InvioFlussi)|

## ***3.2 Tracciato input a SDK***

Il flusso di input avrà formato **csv** posizionale e una naming convention libera a discrezione dell’utente che carica il flusso senza alcun vincolo di nomenclatura specifica (es: nome\_file.csv). Il separatore per il file csv sarà la combinazione di caratteri tra doppi apici: “~“

All’interno della specifica del tracciato sono indicati i dettagli dei campi di business del tracciato di input atteso da SDK, il quale differisce per i sei  flussi dell’area AVN. All’interno di tale file è presente la colonna **Posizione nel file** la quale rappresenta l’ordinamento delle colonne del tracciato di input da caricare all’SDK.


Di seguito la tabella in cui è riportata la specifica del tracciato di input per il flusso in oggetto:


|**Nome campo**|**Posizione nel File**|**Key**|**Descrizione**|**Tipo** |**Obbligatorietà**|**Informazioni di Dominio**|**Lunghezza campo**|**XPATH Tracciato Output**|
| :- | :- | :- | :-: | :-: | :-: | :-: | :-: | :-: |
|Modalita|0| |Campo tecnico utilizzato per distinguere le modalità di invio di: <br>schede vaccinali di soggetti trasferiti ad altra regione per cambio di residenza <br>oppure <br>singole vaccinazioni effettuate da soggetti non residenti in regione.|A|OBB|Deve essere utilizzato il codice “TR” per la trasmissione delle informazioni anagrafiche relative alla scheda vaccinale dei soggetti trasferiti in  altra regione per un cambio di residenza,  rispetto alla regione che sta trasmettendo.<br>Deve essere utilizzato il codice “MV” per la trasmissione delle informazioni anagrafiche relative ad eventi vaccinali effettuati a soggetti non  residenti (domiciliati sanitari, eventi occasionali) nella regione che sta trasmettendo. |2|<br><br>**/vaccinazioniSomministrate/@Modalita** (Prestazione)|
|Tipo Trasmisione|1| |Campo tecnico utilizzato per distinguere trasmissioni di informazioni nuove, modificate o eventualmente annullate|A|OBB|Valori ammessi: <br>I: Inserimento (per la trasmissione di informazioni nuove o per la ritrasmissione di informazioni precedentemente scartate dal sistema di acquisizione);<br>V: Variazione (per la trasmissione di informazioni per le quali si intende far effettuare una sovrascrittura dal sistema di acquisizione);<br>C: Cancellazione (per la trasmissione di informazioni per le quali si intende far effettuare una cancellazione dal sistema di acquisizione).|1|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@TipoTrasmissione**|
|Codice Regione|2|KEY|Individua la Regione che trasmette il dato. |AN|OBB|Il codice da utilizzare è quello a tre caratteri definito con DM 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n.240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.I valori ammessi sono quelli riportati all’**Allegato 1 – Regioni**|3|<br><br>**/vaccinazioniSomministrate/@CodiceRegione** |
|Codice Identificatico dell’Assistito|3|KEY|Codice identificativo dell’assistito|AN|OBB|Il campo deve avere lunghezza massima di 20 caratteri in input alla procedura di cifratura che produrrà un output di massimo 172 caratteri.<br>Le modalità di alimentazione del presente campo sono descritte nel paragarfo 3.6 Codice Identificativo dell’ Assistito – procedura di cifratura|172|**/informazioniAnagrafiche/Assistito/IdAssistito**|
|Tipologia Erogatore|4| |Tipologia di erogatore che ha effettuato la vaccinazione|AN|OBB|Valori ammessi:<br>0 - Istituto di ricovero pubblico o privato<br>1 - Altra struttura sanitaria pubblica o privata accreditata (ambulatori, laboratori, strutture residenziali e semiresidenziali di assistenza sanitaria agli anziani, a pazienti con disabilità, hospice, consultori, strutture per la salute mentale, SERT,Centri Vaccinali Primula, Centri Vaccinali di Popolazione, etc.)<br>2- Centro vaccinale<br>3- Medico di Medicina Generale (MMG)<br>4 – Pediatra di Libera Scelta (PLS)<br>5 – Specialista<br>6– altro<br>7– Struttura privata autorizzata non accreditata  SSN<br>8- Struttura di riabilitazione<br>9 - Strutture che non effettuano attività sanitaria <br>10 – Struttura Militare<br>11 – Ambulatorio nei luoghi di lavoro<br>12- Farmacia<br>99 – Dato non disponibile|2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@TipoErogatore**|
|Codice Struttura|5| |Indica il codice della struttura presso la quale è stata effettuata la vaccinazione oppure a cui appartiene l'erogatore che ha effettuato la vaccinazione|AN|NBB|Valori di riferimento:<br>Se la tipologia di erogatore indicata è 0 – Istituto di ricovero pubblico o privato rappresenta il codice struttura (dei modelli HSP11 , HSP11bis (di cui al decreto ministeriale 5 dicembre 2006, pubblicato nella Gazzetta Ufficiale n. 22 del 27 gennaio 2007, e s.m.i) per Istituti di ricovero pubblici o privati. Ciascun codice è composto da 8 caratteri (campo B del modello HSP11 o campo B + Campo C del modello HSP11Bis) dei quali i primi tre identificano la regione/PA di appartenenza, i successivi tre sono costituiti da un progressivo numerico attribuito in ambito regionale, gli ultimi due costituiscono un eventuale ulteriore progressivo per individuare la singola struttura/stabilimento afferente al complesso ospedaliero;<br>Se la tipologia di erogatore indicata è 1 – Altra struttura sanitaria pubblica o privata accreditata rappresenta il codice struttura (campo D) dei modelli STS11 (di cui al decreto ministeriale 5 dicembre 2006), per altra struttura sanitaria pubblica o privata accreditata (ambulatori, laboratori, strutture residenziali e semiresidenziali di assistenza sanitaria agli anziani, a pazienti con disabilità, hospice, consultori, strutture per la salute mentale, SERT, etc.). Ciascun codice è composto da 6 caratteri <br>Se la tipologia di erogatore indicata è 2 – Centro Vaccinale rappresenta il codice dell’azienda sanitaria a cui afferisce il centro vaccinale.<br>Ciascun codice è composto da 6 caratteri.(codice regione + codice asl)<br>Se la tipologia di erogatore indicata è 3-MMG o 4-PLS o 5-Specialista , 7 – Struttura privata non accreditata, 9- Strutture che non effettua attività sanitaria o 10 – Struttura Militare o 11 – Ambulatorio nei luoghi di lavoro o 12- Farmacia rappresenta il codice dell’azienda sanitaria di iscrizione del MMG/PLS o di riferimento territoriale della struttura privata non accreditata o della Struttura Militare<br>Ciascun codice è composto da 6 caratteri .(codice regione + codice asl)<br>Se la tipologia di erogatore indicata è 8 – Centro di riabilitazione rappresenta il codice struttura (campo B) dei modelli RIA11 (di cui all’ex art. 26 L. 833/78)<br>Ciascun codice è composto da 6 caratteri<br>Se la tipologia di erogatore indicata è  6 – Altro oppure 99- Dato non disponibile, il campo non deve essere valorizzato  |8|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@CodiceStruttura**|
|Condizioni sanitarie a rischio|6| |Indica le condizioni sanitarie del soggetto da vaccinare per cui è raccomandata la vaccinazione (es. Diabete, HIV, Emodializzato...), coerenti con le categorie previste dal PNPV vigente e con il Piano vaccinazioni anti-Covid19|AN|OBB|I valori ammessi sono quelli riportati **all’Allegato 2 - Condizione sanitarie a rischio** |2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@CodCondizioneSanitaria**|
|Categoria a rischio |7| |Indica la categoria a rischio per la quali viene raccomandata la vaccinazione (esposizione lavorativa, stile di vita, viaggi..), coerenti con le categorie previste dal PNPV vigente e con il Piano vaccinazioni anti-Covid19|AN|OBB|I valori ammessi sono quelli riportati **all’Allegato 3 – Categorie a rischio**|2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@CodCategoriaRischio**|
|Codice AIC|8| |Codice di autorizzazione immissione in commercio in Italia del vaccino rilasciato dall’ AIFA (AIC)<br>In caso di vaccini esteri, rappresenta il codice con cui viene riconosciuto in Italia.|AN|NBB (Obbligatorio per eventi vaccinali successivi alla data del 01/07/2019)|Il valore ammesso è il codice a 9 cifre concesso dall’Agenzia Italiana del Farmaco (AIFA) al momento del rilascio dell'autorizzazione all’immissione in commercio di un medicinale ad uso umano.<br>Gli attuali bollini farmaceutici presenti su ogni confezione di un medicinale presentano, nella zona alta del medesimo bollino, un codici a barre 39 (conforme al decreto del Ministro della Sanità 2 agosto 2001, pubblicato nella Gazzetta Ufficiale n. 202 del 31 agosto 2001), che consente la lettura ottica del codice AIC.<br>In caso di vaccini esteri è il codice a 9 cifre “E+8 cifre numeriche” che si può recuperare dal portale opendata del Ministero della salute all’indirizzo: http://www.dati.salute.gov.it/|9|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@CodiceAICVaccino**|
|Denominazione Vaccino|9| |Indica il nome commerciale del vaccino somministrato|AN|NBB (Obbligatorio se non viene indicato il codice AIC per eventi vaccinali successivi alla data del 01/07/2019)|Valorizzare sulla base degli elenchi dei vaccini autorizzati resi disponibili da parte dell’AIFA oppure in caso di vaccini esteri sulla base dell’elenco pubblicato sul portale opendata del Ministero della salute all’indirizzo: http://www.dati.salute.gov.it/|100|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@DenomVaccino**|
|Tipo Formulazione|10| |Indica il tipo di formulazione del vaccino|AN|OBB|I valori ammessi sono quelli riportati all’Allegato 4 – Tipologie Formulazione<br>Nel caso in cui il vaccino somministrato non sia la combinazione di più antigeni riportati nell’allegato 4 valorizzare con Monovalente |2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@CodTipoFormulazione**|
|Via di Somministrazione|11| |Indica la via di somministrazione del vaccino|AN|OBB|I valori ammessi sono:<br>01 - Intramuscolo<br>02 - Sottocutaneo<br>03 - Intradermico<br>04 - Orale<br>05 – Altro<br>99 – Dato non disponibile|2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@ViaSomministrazione**|
|Lotto|12| |Indica il numero di lotto di produzione del vaccino|AN|NBB (Obbligatorio per eventi vaccinali successivi alla data del 01/07/2019)|Il valore ammesso è il numero che l’azienda produttrice del medicinale stampa sulle confezioni in maniera tale che, tutte quelle prodotte nello stesso ciclo di lavorazione, siano facilmente riconoscibili|40|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@LottoVaccino**|
|Data Scadenza |13| |Indica la data di scadenza del vaccino riportata sulla confezione|AN|NBB (Obbligatorio per eventi vaccinali successivi alla data del 01/07/2019)|Formato: AAAA-MM-GG|10|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@DataScadenza**|
|Modalità Pagamento|14| |Indica le modalità di pagamento del vaccino|AN|OBB|I valori ammessi sono:<br>01 - Vaccinazione a carico SSN<br>02 -Vaccinazione in compartecipazione alla spesa o prezzo sociale<br>03 - Pagamento integrale a carico dell’assistito<br>99 – Dato non disponibile|2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@ModalitaPagamento**|
|Data Somministrazione|15|KEY|Indica la data di somministrazione del vaccino|D|OBB|Formato: AAAA-MM-GG|10|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@DataSomministrazione**|
|Sito Inoculazione|16| |Indica la sede corporea in cui è stato somministrato il vaccino|AN|OBB|I valori ammessi sono:<br>01 - deltoide sinistro<br>02 - deltoide destro<br>03 - quadricipite della coscia sinistra<br>04 - quadricipite della coscia destra<br>05 - gluteo sinistro<br>06 - gluteo destro<br>07 – Altro<br>99 -  Dato non disponibile|2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@SitoInoculazione**|
|Codice Comune Somministrazione|17| |Comune in cui è localizzata la struttura o il centro vaccinale distrettuale o sub distrettuale in cui è stata eseguita la vaccinazione|AN|NBB <br>(Obbligatorio per eventi vaccinali successivi alla data di entrata in vigore del decreto AVN 01/01/2019)|Formato: NNNNNN<br>Il codice da utilizzare è il codice secondo codifica ISTAT, i cui primi tre caratteri individuano la provincia e i successivi un progressivo all’interno di ciascuna provincia che individua il singolo comune. <br>In caso di vaccinazione effettuata all’estero va indicato il codice 999999|6|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@ComuneSomministrazione**|
|Codice ASL Somministrazione|18| |Indicare il codice della ASL a cui afferisce la struttura o il centro vaccinale distrettuale o sub distrettuale in cui è stata eseguita la vaccinazione|AN|NBB <br>(Obbligatorio per eventi vaccinali successivi alla data di entrata in vigore del decreto AVN 01/01/2019)|Il campo deve essere valorizzato con i codici a tre caratteri della ASL (di cui al D.M. 05/12/2006 e successive modifiche - Anagrafica MRA fase 1) utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali.<br>In caso di vaccinazione effettuata all’estero va indicato il codice 999|3|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@AslSomministrazione**|
|Codice Regione Somministrazione|19| |Indica il codice della regione a cui afferisce la struttura o il centro vaccinale distrettuale o sub distrettuale in cui è stata eseguita la vaccinazione|AN|NBB <br>(Obbligatorio per eventi vaccinali successivi alla data di entrata in vigore del decreto AVN 01/01/2019)|Formato: NNN<br>I valori ammessi sono quelli a tre caratteri definiti con decreto del Ministero della sanità del 17 settembre 1986, pubblicato nella Gazzetta Ufficiale n. 240 del 15 ottobre 1986, e successive modifiche, utilizzato anche nei modelli per le rilevazioni delle attività gestionali ed economiche delle Aziende unità sanitarie locali. In caso di vaccinazione effettuata all’estero va indicato il codice 999.|3|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@RegioneSomministrazione**|
|Stato Estero Somministrazione|20| |Indica lo stato presso cui è stata somministrata la vaccinazione.|AN|NBB <br>(Obbligatorio per eventi vaccinali successivi alla data di entrata in vigore del decreto AVN 01/01/2019)|La codifica da utilizzare è quella Alpha2 (a due lettere) prevista dalla normativa ISO 3166-2.<br>Se compilato con uno stato diverso da IT (Italia), compilare gli altri attributi afferenti la residenza nel modo seguente:<br>Regione di somministrazione=999<br>ASL di somministrazione=999<br>Comune di somministrazioe=999999<br>Ulteriori valori ammessi:<br>XK = Kosovo<br>XX = Stato residenza sconosciuto;<br>ZZ = Apolidi.|2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/@StatoEsteroSomministrazione**|
|Antigene |21|KEY|Indica il singolo antigene/principio vaccinale che costituisce il vaccino somministrato|AN|OBB|Valorizzare sulla base degli elenchi dei vaccini autorizzati resi disponibili da parte dell’AIFA. <br>In caso di vaccini combinati indicare i singoli antigeni che compongono il vaccino.<br>Ad esempio in caso di somministrazione di vaccino trivalente MPR (Morbillo/Parotite/Rosolia) dovranno essere inseriti tre record.<br>I valori ammessi sono quelli riportati all’Allegato 5 – Antigeni/Principi Vaccinali|2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/PrincipioVaccinale/@CodAntigene**|
|Dose|22|KEY|Indica il numero di dose somministrata rispetto al calendario vaccinale per il singolo antigene/principio vaccinale|N|OBB|I valori ammessi sono da 1 a 99|Max 2|**/vaccinazioniSomministrate/Assistito/VaccinoSomministrato/PrincipioVaccinale/@Dose**|


## ***3.3 Controlli di validazione del dato (business rules)***

Di seguito sono indicati i controlli da configurare sulla componente di Validation Engine e rispettivi error code associati riscontrabili sui dati di input per il flusso VSM.

Gli errori sono solo di tipo scarti (mancato invio del record).

Al verificarsi anche di un solo errore di scarto, tra quelli descritti, il record oggetto di controllo sarà inserito tra i record scartati.

Business Rule non implementabili lato SDK:

- Storiche (Business Rule che effettuano controlli su dati già acquisiti/consolidati che non facciano parte del dato anagrafico)
- Transazionali (Business Rule che effettuano controlli su record, i quali rappresentano transazioni, su cui andrebbe garantito l’ACID (Atomicità-Consistenza-Isolamento-Durabilità))
- Controllo d’integrità (cross flusso) (Business Rule che effettuano controlli sui record utilizzando informazioni estratte da record di altri flussi)


Di seguito le BR per il flusso in oggetto:


|**CAMPO**|**FLUSSO**|**CODICE ERRORE**|**FLAG ATTIVAZIONE**|**DESCRIZIONE ERRORE**|**DESCRIZIONE ALGORITMO**|**TABELLA ANAGRAFICA**|**CAMPI DI COERENZA**|**SCARTI/ANOMALIE**|**TIPOLOGIA BR**|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|Modalità|VSM|1801|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Modalità|VSM|1802|ATTIVA|Non appartenenza al dominio di riferimento|Valori diversi da quelli ammessi : TR, MV| | |scarti|Basic|
|Modalità|VSM|1803|ATTIVA|Modalità non coerente con il flusso inviato|in caso di flusso Trasferimenti/Mobilità il naming del flusso conterrà TM e la modalità è diversa da TR o MV| | |scarti|Basic|
|Tipo Prestazione|VSM|1901.2|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Tipo Prestazione|VSM|1902.2|ATTIVA|Non appartenenza al dominio di riferimento|Valori diversi da quelli ammessi : I,i,V,v,C,c| | |scarti|Basic|
|Codice Regione |VSM|1921|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Codice Regione |VSM|1922|ATTIVA|Non appartenenza al dominio di riferimento|Il valore inserito e controllato non è presente in anagrafica regioni|Anagrafiche: REGIONE| |scarti|Anagrafica|
|Codice Regione |VSM|1905|ATTIVA|Il codice regione non coincide con la regione inviante.|Il campo Codice Regione non coincide con la regione che sta trasmettendo il file. | | |scarti|Basic|
|Codice Identificatico dell’Assistito|VSM|1701|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Codice Identificatico dell’Assistito|VSM|1703|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 172 caratteri| | |scarti|Basic|
|Tipologia Erogatore|VSM|5001|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML o attributo non presente o tag XML o attributo  presente ma non valorizzato.| | |scarti|Basic|
|Tipologia Erogatore|VSM|5002|ATTIVA|Non appartenenza al dominio di riferimento|Valore diverso da quelli  ammessi:0,1,2,3,4,5,6,7,8,9,10,11,12,99| | |scarti|Basic|
|Codice Struttura|VSM|2011(Ex. 2010)|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza non compresa tra 0 e 8 caratteri| | |scarti|Basic|
|Codice Struttura|VSM|3005|ATTIVA|Mancata valorizzazione del codice struttura|Il valore è nullo e il campo Tipologia erogatore è diverso da “6” e da “99”’| | |scarti|Basic|
|Codice Struttura|VSM|3010|ATTIVA|Codice struttura non presente nell’anagrafica HSP11|Se il tipo erogatore è valorizzato con 0 il codice non è presente nei modelli HSP11 , HSP11bis (di cui al decreto ministeriale 5 dicembre 2006, pubblicato nella Gazzetta Ufficiale n. 22 del 27 gennaio 2007, e s.m.i) -- Istituti di ricovero a 8 cifre|Anagrafiche: HPS11| |scarti|Anagrafica|
|Codice Struttura|VSM|3015|ATTIVA|Codice struttura non presente nell’anagrafica STS11|Se il tipo erogatore è valorizzato con **1** il codice non è presente nei modelli STS11** (di cui al decreto ministeriale 5 dicembre 2006) a 6 cifre|Anagrafiche: STS11| |scarti|Anagrafica|
|Codice Struttura|VSM|3020|ATTIVA|Codice struttura non presente nell’anagrafica ASL – MRA Fase 1|Se il tipo erogatore è valorizzato con **2 o 3 o 4 o 5 o 7 o 10 o 11 o 12** il codice non è presente nell’anagrafe di riferimento (D.M. 05/12/2006 e successive modifiche - Anagrafica MRA fase 1) – 6 cifre|Anagrafiche: MRA | |scarti|Anagrafica|
|Codice Struttura|VSM|3021|ATTIVA|Codice struttura non presente nell’anagrafica RIA11|Se il tipo erogatore è valorizzato con **8** il codice non è presente nei modelli RIA11** (di cui all’ex art. 26 L. 833/78)) a 6 cifre|Anagrafiche: RIA11| |scarti|Anagrafica|
|Condizioni sanitarie a rischio|VSM|6001|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML o attributo non presente o tag XML o attributo  presente ma non valorizzato.| | |scarti|Basic|
|Condizioni sanitarie a rischio|VSM|6000|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri se valorizzato| | |scarti|Basic|
|Condizioni sanitarie a rischio|VSM|3030|ATTIVA|Codice Condizioni Sanitarie a rischio non presente in anagrafica| |Condizioni sanitarie a rischio| |scarti|Anagrafica|
|Categoria a rischio|VSM|6010|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML o attributo non presente o tag XML o attributo  presente ma non valorizzato.| | |scarti|Basic|
|Categoria a rischio|VSM|6011|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri se valorizzato| | |scarti|Basic|
|Categoria a rischio|VSM|5025|ATTIVA|Codice categorie a rischio non presente in anagrafica |Se il codice con è presente nell’anagrafe di riferimento – Allegato 3 delle presenti specifiche|Categorie a rischio| |scarti|Anagrafica|
|Categoria a rischio|VSX|5026|ATTIVA|Codice categorie a rischio non coerente con Antigene 47 – Vaiolo e Vaiolo delle scimmie|Codice categorie a rischio diverso da “01” e Antigene Valorizzato con “47”| |Antigene|Scarti|Basic|
|Codice AIC|VSM|6020|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 9 caratteri se valorizzato| | |scarti|Basic|
|Codice AIC|VSM|5020|ATTIVA|Codice AIC non valorizzato|Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva al **01/07/2019** e lo stato estero di somministrazione è nullo oppure uguale a ‘IT’| | |scarti|Basic|
|Codice AIC|VSM|3035|ATTIVA|Codice AIC non presente nelle anagrafiche di riferimento AIFA|Se il codice non è presente nell’anagrafe di riferimento dei farmaci/vaccini di AIFA o in caso di vaccini esteri nell’elenco pubblicato dal Ministero della Salute.|AIFA+ELENCO MdS| |scarti|Anagrafica|
|Denominazione Vaccino|VSM|6060|ATTIVA|Lunghezza maggiore di quella attesa|La lunghezza è maggiore di 100 caratteri se valorizzato| | |scarti|Basic|
|Denominazione Vaccino|VSM|3040|ATTIVA|Denominazione Vaccino non valorizzata|Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva al **01/07/2019**| | |scarti|Basic|
|Tipo formulazione|VSM|6031|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Tipo formulazione|VSM|6030|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri se valorizzato| | |scarti|Basic|
|Tipo formulazione|VSM|3055|ATTIVA|Tipo Formulazione non presente nelle anagrafiche di riferimento |Se il codice con è presente nell’anagrafe di riferimento – Allegato 4 delle presenti specifiche|Tipologie Formulazione| |scarti|Anagrafica|
|Tipo formulazione|VSM|3060|ATTIVA|Mancata coerenza tra tipo formulazione e antigeni|Il tipo di formulazione non è coerente con il numero degli antigenti indicati (ES. Monovalente e presenza di 2 antigeni oppure Trivalente e presenza di un solo antigene) e la data di somministrazione del vaccino è valorizzata e successiva al 01/07/2019 <br><br>**Descrizione Approfondita:**<br><br>Dato un record su cui il valore del campo tipo formulazione può essere uno dei seguenti:<br><br>- 01 (MONOVALENTE)<br>- 02 (BIVALENTE)<br>- 03 (BIVALENTE)<br>- 04 (TETRAVALENTE)<br>- 05 (PENTAVALENTE)<br>- 06 (ESAVALENTE):<br><br>Dopo aver convertito il valore del tipo formulazione (Es. 06) in numero **n** (Es. 6), occorre controllare che siano presenti **n-1** record all'interno del file che abbiano gli stessi valori su <br>**codice regione, codice identificativo assistito, data somministrazione, dose  e tipo trasmissione prestazione**. <br>Per esempio se un assistito effettua una vaccinazione con tipo formulazione Bivalente allora devono essere presenti per la stessa vaccinazione 2 antigeni. | | |scarti|Basic|
|Via di somministrazione|VSM|6041|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Via di somministrazione|VSM|6040|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri se valorizzato| | |scarti|Basic|
|Via di somministrazione|VSM|6042|ATTIVA|Non appartenenza al dominio di riferimento|Il valore è diverso dai vaolori ammessi:01,02,03,04,05,99| | |scarti|Basic|
|Lotto|VSM|6043|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza deve essere compresa tra 1 e 40 caratteri se valorizzato| | |scarti|Basic|
|Lotto|VSM|3070|ATTIVA|Lotto non valorizzato|Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva al **01/07/2019**| |data somministrazione|scarti|Basic|
|Data Scadenza|VSM|6050|ATTIVA|Datatype errato|Il campo deve essere valorizzato con il formato data AAAA-MM-GG| | |scarti|Basic|
|Data Scadenza|VSM|3075|ATTIVA|Data scadenza non valorizzata|Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva al **01/07/2019**| |data somministrazione|scarti|Basic|
|Modalità di pagamento|VSM|6061|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Modalità di pagamento|VSM|6063 (Ex 6060)|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri se valorizzato| | |scarti|Basic|
|Modalità di pagamento|VSM|6062|ATTIVA|Non appartenenza al dominio di riferimento|Il valore è diverso dai vaolori ammessi:01,02,03,99| | |scarti|Basic|
|Data somministrazione|VSM|6071|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML o attributo non presente o tag XML o attributo  presente ma non valorizzato.| | |scarti|Basic|
|Data somministrazione|VSM|6070|ATTIVA|Datatype errato|Il campo deve essere valorizzato con il formato data AAAA-MM-GG| | |scarti|Basic|
|Data somministrazione|VSM|4000|ATTIVA|Incoerenza tra data somministrazione e data scadenza del vaccino |La data somministrazione > data scadenza del vaccino| |data scadenza vaccino|scarti|Basic|
|Sito Inoculazione|VSM|6081|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Sito Inoculazione|VSM|6080|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri se valorizzato| | |scarti|Basic|
|Sito Inoculazione|VSM|6082|ATTIVA|Non appartenenza al dominio di riferimento|Il valore è diverso da quelli ammessi:01,02,03,04,05,06,07,99| | |scarti|Basic|
|Sito Inoculazione|VSM|4001|ATTIVA|Sito Inoculazione non congruo|Il valore è uguale a 07-Altro o 99-Dato non Disponibile  e la Via di somministrazione è diversa da 4-Orale, 5-Altro o 99-Dato non disponibile | |via di somministrazione|scarti|Basic|
|Codice Comune Somministrazione|VSM|6090|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 6 caratteri se valorizzato| | |scarti|Basic|
|Codice Comune Somministrazione|VSM|4005|ATTIVA|Comune di somministrazione non valorizzato|Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva al 01/01/2019 | |Data somministrazione|scarti|Basic|
|Codice Comune Somministrazione|VSM|4010|ATTIVA|Codice comune non presente nel dominio di riferimento|Il codice è diverso da 999999 (somministrato in Italia)  e non è secondo codifica ISTAT, i cui primi tre caratteri individuano la provincia e i successivi un progressivo all’interno di ciascuna provincia che individua il singolo comune. Il controllo non viene effettuato rispetto alla data di somminstrazione.|Comuni Istat| |scarti|Anagrafica|
|Codice Comune Somministrazione|VSM|4015|ATTIVA|Comune di Somministrazione incoerente con Stato Estero di Somministrazione|Il codice è uguale a 999999 (residenti all’estero)  e lo stato estero di somministrazione è uguale a ‘IT’| |stato estero somministrazione|scarti|Basic|
|Codice Comune Somministrazione|VSM|4020|ATTIVA|Comune di somministrazione incoerente con Regione o ASL  di Somministrazione|Il codice è diverso da 999999 e nel dominio e il codice  ASL di somministrazione e/o  la Regione di somministrazione sono valorizzati con 999 oppure sono valorizzati con valori che non afferiscono al comune di somministrazione. Il controllo non viene effettuato rispetto alla data di somminstrazione.|tabella di raccordo regioni-province-comuni-asl| |scarti|Anagrafica|
|Codice ASL Somministrazione|VSM|6100|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 3 caratteri se valorizzato| | |scarti|Basic|
|Codice ASL Somministrazione|VSM|4025|ATTIVA|ASL di somministrazione non valorizzata|Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva al 01/01/2019.| | |scarti|Basic|
|Codice ASL Somministrazione|VSM|4030|ATTIVA|Codice ASL  non presente nel dominio di riferimento|Il codice è diverso da 999 e non presente nell’anagrafe di riferimento (D.M. 05/12/2006 e successive modifiche - Anagrafica MRA fase 1).Il controllo non viene effettuato rispetto alla data di somminstrazione.|Anagrafiche: ASL| |scarti|Anagrafica|
|Codice ASL Somministrazione|VSM|4035|ATTIVA|Asl di Somministrazione incoerente con Stato Estero di Somministrazione|Il codice è uguale a 999 (residenti all’estero)  e lo stato estero di Somministrazione è uguale a ‘IT’| | |scarti|Basic|
|Codice ASL Somministrazione|VSM|4040|ATTIVA|Asl di Somministrazione incoerente con Regione o Comune  di Somministrazione|Il codice è diverso da 999 e nel dominio e il codice  Comune di somministrazione è valorizzato con 999999 e/o  la Regione di somministrazione è valorizzata con 999 oppure sono valorizzati con valori che non afferiscono alla asl di somministrazione. Il controllo non viene effettuato rispetto alla data di somminstrazione.|tabella di raccordo regioni-province-comuni-asl|Regione e comune di somministrazione|scarti|Anagrafica|
|Codice Regione Somministrazione|VSM|6110|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 3 caratteri| | |scarti|Basic|
|Codice Regione Somministrazione|VSM|4045|ATTIVA|Regione di somministrazione non valorizzata|Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva al  01/01/2019| | |scarti|Basic|
|Codice Regione Somministrazione|VSM|4050|ATTIVA|Codice regione non presente nel dominio di riferimento|Il codice è diverso da 999 e non presente nell’anagrafe di riferimento delle regioni. Il controllo non viene effettuato rispetto alla data di somminstrazione.|Anagrafiche: REGIONE| |scarti|Anagrafica|
|Codice Regione Somministrazione|VSM|4055|ATTIVA|Regione di Somministrazione incoerente con Stato Estero di Somministrazione|Il codice è uguale a 999 (residenti all’estero)  e lo stato estero di Somministrazione è uguale a ‘IT’| |Stato Estero di somministrazione|scarti|Basic|
|Codice Regione Somministrazione|VSM|4060|ATTIVA|Regione di Somministrazione incoerente con Comune o ASL  di Somministrazione|Il codice è diverso da 999 e nel dominio e il codice  Comune di Somministrazione è valorizzato con 999999 e/o  la Asl di Somministrazione è valorizzata con 999 oppure sono valorizzati con valori che non afferiscono alla regione di somministrazione. Il controllo non viene effettuato rispetto alla data di somminstrazione. |tabella di raccordo regioni-province-comuni-asl|codice comune somministrazione, asl somministrazione|scarti|Anagrafica|
|Codice Regione Somministrazione|VSM|4065|ATTIVA|Incoerenza tra regione di somministrazione, regione che ha trasmesso il dato e modalità di trasmissione|La regione di somministrazione è diversa dalla regione di trasmissione del dato e la modalità è uguale a MV| |regione di trasmissione|scarti|Basic|
|Codice Regione Somministrazione|VSM|4070|ATTIVA|Incoerenza tra regione di somministrazione, regione di residenza o di domicilio sanitario dell’assistito e modalità  di trasmissione|La regione di somministrazione è uguale  alla regione di residenza oppure alla regione di domicilio e la modalità è uguale a MV| |Regione di Residenza, regione domicilio, modalità|scarti|Basic|
|Stato Estero di somministrazione|VSM|6120|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri| | |scarti|Basic|
|Stato Estero di somministrazione|VSM|4075|ATTIVA|Stato di somministrazione non valorizzato |Il valore è nullo e la data di somministrazione del vaccino è valorizzata e successiva 01/01/2019| |data somministrazione|scarti|Basic|
|Stato Estero di somministrazione|VSM|4080|ATTIVA|Codice stato non presente nel dominio di riferimento|Il codice non è presente nella codifica Alpha2 (a due lettere) prevista dalla normativa ISO 3166-2.|Codifica Alpha2| |scarti|Anagrafica|
|Stato Estero di somministrazione|VSM|4085|ATTIVA|Stato estero di somministrazione diverso da “IT” incoerente con Regione, comune e ASL di somministrazione|Se compilato e diverso da IT (Italia), e Regione di somministrazione diverso da 999 e/o ASL di somministrazione diverso da 999 e/o Comune di somministrazione diverso da 999999| | |scarti|Basic|
|Stato Estero di somministrazione|VSM|4090|ATTIVA|Stato estero di somministrazione uguale a “IT” incoerente con Regione, comune e ASL di somministrazione|Se compilato e uguale a IT (Italia), e Regione di somministrazione uguale a 999 e/o ASL di somministrazione uguale a 999 e/o Comune di somministrazione uguale a 999999| | |scarti|Basic|
|Antigene|VSM|6141|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Antigene|VSM|6140|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è diversa da 2 caratteri| | |scarti|Basic|
|Antigene|VSM|4095|ATTIVA|Antigene non presente nelle anagrafiche di riferimento |Se il codice con è presente nell’anagrafe di riferimento – Allegato 5 delle presenti specifiche|Antigeni/Principi Vaccinali| |scarti|Anagrafica|
|Antigene|VSM|4100|ATTIVA|Antigene non valorizzato in modo corretto |Il valore è uguale a 08-INFLUENZA oppure 09 – ERPES ZOSTER e la data di somministrazione del vaccino è valorizzata e successiva al 01/01/2019| | |scarti|Basic|
|Dose|VSM|6051|ATTIVA|Mancata valorizzazione di un campo obbligatorio|Tag XML non presente o tag XML presente ma non valorizzato.| | |scarti|Basic|
|Dose|VSM|6052(Ex. 6050)|ATTIVA|Lunghezza diversa da quella attesa|La lunghezza è al massimo di 2 caratteri| | |scarti|Basic|



## ***3.4 Accesso alle anagrafiche***

I controlli applicativi saranno implementati a partire dall’acquisizione dei seguenti dati anagrafici disponibili in ambito MdS e recuperati con servizi ad hoc (Service Layer mediante PDI):

- REGIONE
- ASL
- tabella di raccordo regioni-province-comuni-asl
- Codifica Alpha2
- HPS11
- STS11
- MRA
- RIA11
- Condizioni sanitarie a rischio
- Categorie a rischio
- AIFA
- ELENCO MdS
- Tipologie Formulazione
- Antigeni
- Principi Vaccinali
- Comuni Istat


Il dato anagrafico sarà presente sottoforma di tabella composta da tre colonne:

- Valore (in cui è riportato il dato, nel caso di più valori, sarà usato il carattere # come separatore)


- Data inizio validità (rappresenta la data di inizio validità del campo Valore)
 - Formato: AAAA-MM-DD
 - Notazione inizio validità permanente: **1900-01-01**


- Data Fine Validità (rappresenta la data di fine validità del campo Valore)
  - Formato: AAAA-MM-DD
  - Notazione fine validità permanente: **9999-12-31**

Affinchè le Business Rule che usano il dato anagrafico per effettuare controlli siano correttamente funzionanti, occorre controllare che la data di competenza del record su cui si effettua il controllo (la quale varia in base al flusso), sia compresa tra le data di validità.  Tutte le tabelle anagrafiche hanno dei dati con validità permanente ad eccezione delle seguenti per le quali sono previste date di validità specifiche:

- Tabella di raccordo regioni-province-comuni-asl
- Comuni Istat
- Codifica Alpha2
- Anagrafiche: ASL
- Anagrafiche: REGIONE

Di seguito viene mostrato un caso limite di anagrafica in cui sono presenti delle sovrapposizioni temporali e contraddizioni di validità permanente/specifico range:


|ID|VALUE|VALID\_FROM|VALID\_TO|
| - | - | - | - |
|1|VALORE 1|1900-01-01|9999-12-31|
|2|VALORE 1|2015-01-01|2015-12-31|
|3|VALORE 1|2018-01-01|2023-12-31|
|4|VALORE 1|2022-01-01|2024-12-31|


Diremo che il dato presente sul tracciato di input è valido se e solo se:

∃ VALUE\_R = VALUE\_A “tale che” VALID\_FROM<= **DATA\_COMPETENZA** <= VALID\_TO

(Esiste almeno un valore compreso tra le date di validità)

Dove:

- VALUE\_R rappresenta i campi del tracciato di input coinvolti nei controlli della specifica BR

- VALUE\_A rappresenta i campi dell’anagrafica coinvolti nei controlli della specifica BR

- VALID\_FROM/VALID\_TO rappresentano le colonne dell’anagrafica

- DATA\_COMPETENZA data da utilizzare per il filtraggio del dato anagrafico specifica per flusso.



La DATA\_COMPETENZA da utilizzare per filtrare il dato anagrafico per il flusso VSM sulle tabelle con date di validità specifiche riportate in precedenza è l’anno della data somministrazione del record.

## ***3.5 Alimentazione MdS***

### **3.5.1 Invio Flussi**

A valle delle verifiche effettuate dal Validation Engine, qualora il caricamento sia stato effettuato con il parametro Tipo Elaborazione impostato a P, verranno inviati verso MdS tutti i record corretti secondo le regole di validazione impostate.

Verrà richiamata la procedura invioFlussi di GAF WS (tramite PDI) alla quale verranno passati in input i parametri così come riportati nella seguente tabella:



|**NOME PARAMETRO**|**VALORIZZAZIONE**|
| :- | :- |
|ANNO RIFERIMENTO|Parametro ANNO RIFERIMENTO in input a SDK|
|PERIODO RIFERIMENTO|Parametro PERIODO RIFERIMENTO in input a SDK |
|CATEGORIA FLUSSI|AVT|
|NOME FLUSSO|VSM (Vaccinazione Effettuate)|
|NOME FILE|Parametro popolato dall’SDK in fase di invio flusso con il nome file generato dal Validation Engine in fase di produzione file.|


### **3.5.2 Flussi di Output per alimentazione MdS**


I flussi generati dall’SDK saranno presenti sotto la cartella /sdk/xml\_output e dovranno essere salvati e inviati verso MdS rispettando la seguente nomenclatura:


**SDK\_AVT\_VSM\_\_13\_{ID\_RUN}.xml**


Dove:

- ID\_RUN rappresenta l’identificativo univoco


### **3.5.3 Gestione Risposta Mds**

A valle della presa in carico del dato da parte di MdS, SDK riceverà una response contenente le seguenti informazioni:

1. **codiceFornitura**: stringa numerica indicante l’identificativo univoco della fornitura inviata al GAF
1. **errorCode**: stringa alfanumerica di 256 caratteri rappresentante il codice identificativo dell’errore eventualmente riscontrato
1. **errorText**: stringa alfanumerica di 256 caratteri rappresentante il descrittivo dell’errore eventualmente riscontrato
1. Insieme delle seguenti triple, una per ogni file inviato:

 a. **idUpload**: stringa numerica indicante l’identificativo univoco del singolo file ricevuto ed accettato dal MdS, e corrispondente al file inviato con la denominazione riportata nell’elemento “nomeFile” che segue.

 b. **esito**: stringa alfanumerica di 4 caratteri rappresentante l’esito dell’operazione (Vedi tabella sotto).

 c. **nomeFile**: stringa alfanumerica di 256 caratteri rappresentante il nome dei file inviati.

Di seguito la tabella di riepilogo dei codici degli esiti possibili dell’invio del file:


|**ESITO**|**DESCRIZIONE**|
| :- | :- |
|AE00|Errore di autenticazione al servizio|
|IF00|Operazione completata con successo|
|IF01|Incongruenza tra CF utente firmatario e cf utente inviante|
|IF02|Firma digitale non valida|
|IF03|Firma digitale scaduta|
|IF04|Estensione non ammessa|
|IF05|Utente non abilitato all’invio per la Categoria Flusso indicata|
|IF06|Utente non abilitato all’invio per il Flusso indicata|
|IF07|Periodo non congurente con la Categoria Flusso indicata|
|IF08|Il file inviato è vuoto|
|IF09|Errore interno al servizio nella ricezione del file|
|IF10|Il numero di allegati segnalati nel body non corrisponde al numero di allegati riscontrati nella request|
|IF11|Il nome dell’allegato riportato nel body non è presente tra gli allegati della request (content-id)|
|IF12|Presenza di nomi file duplicati|
|IF13|Errore interno al servizio nella ricezione del file|
|IF14|Errore interno al servizio nella ricezione del file|
|IF15|Errore interno al servizio nella ricezione del file|
|IF99|Errore generico dell’operation|


Copia dei file inviati verso MdS il cui esito è positivo (ovvero risposta della procedura Invio Flussi con IF00) saranno trasferiti e memorizzati in una cartella ad hoc di SDK (es. /sdk/sent) rispettando la seguente naming:


**SDK\_AVT\_VSM\_\_13\_{ID\_RUN}.xml**


Dove :

- ID\_RUN rappresenta l’identificativo univoco

## ***3.6 Scarti di processamento***

In una cartella dedicata (es. /sdk/esiti) verrà creato un file json contenente il dettaglio degli scarti riscontrati ad ogni esecuzione del processo SDK.

Il naming del file sarà:  ESITO\_{ID\_RUN}.json

Dove:

- ID\_RUN rappresenta l’identificativo univoco dell’elaborazione

Di seguito il tracciato del record da produrre.


|**CAMPO**|**DESCRIZIONE**|
| :- | :- |
|NUMERO RECORD|Numero del record del flusso input|
|RECORD PROCESSATO|Campi esterni rispetto al tracciato, che sono necessari per la validazione dello stesso.</p><p>Record su cui si è verificato uno scarto, riportato in maniera strutturata (nome\_campo-valore).|
|LISTA ESITI|<p>Lista di oggetti contenente l’esito di validazione per ciascun campo:</p><p>- Campo: nome campo su cui si è verificato uno scarto</p><p>- Valore Scarto: valore del campo su cui si è verificato uno scarto</p><p>- Valore Esito: esito di validazione del particolare campo</p><p>- Errori Validazione: contiene i campi Codice (della Business Rule) e Descrizione (della Business Rule)</p>|

## ***3.7 Informazioni dell’esecuzione***

In una cartella dedicata (es. /sdk/run) verrà creato un file contenente il dettaglio degli esiti riscontrati ad ogni esecuzione del processo SDK. Verrà prodotto un file di log per ogni giorno di elaborazione.

Il naming del file sarà:  

{ID\_RUN}.json

Dove:

- ID\_RUN rappresenta l’identificativo univoco dell’elaborazione

Di seguito il tracciato del record da produrre.


|**CAMPO**|**DESCRIZIONE**|
| :- | :- |
|ID RUN (chiave)|Identificativo univoco di ogni esecuzione del SDK|
|ID\_CLIENT|Identificativo Univoco della trasazione sorgente che richiede processamento lato SDK|
|ID UPLOAD (chiave)|Identificativo di caricamento fornito da MdS|
|TIPO ELABORAZIONE|F (full)/R (per singolo record) - Impostato di default a F|
|MODALITA’ OPERATIVA|P (=produzione) /T (=test)|
|DATA INIZIO ESECUZIONE|Timestamp dell’ inizio del processamento|
|DATA FINE ESECUZIONE|Timestamp di completamento del processamento|
|STATO ESECUZIONE |<p>Esito dell’esecuzione dell’ SDK. </p><p>Possibili valori: </p><p>- IN ELABORAZIONE: Sdk in esecuzione;</p><p>- ELABORATA: Esecuzione completata con successo;</p><p>- KO: Esecuzione fallita: </p><p>- KO SPECIFICO: Esecuzione fallita per una fase/componente più rilevante della soluzione (es. ko\_gestione\_file, ko\_gestione\_validazione, ko\_invio\_ministero, etc.); </p><p>- KO GENERICO: un errore generico non controllato.</p>|
|FILE ASSOCIATI RUN|nome del file di input elaborato dall’SDK|
|NOME FLUSSO|valore fisso che identifica lo specifico SDK in termini di categoria e nome flusso|
|NUMERO RECORD |Numero di record del flusso input|
|NUMERO RECORD ACCETTATI|Numero validi|
|NUMERO RECORD SCARTATI|Numero scarti|
|VERSION|Versione del SDK (Access Layer e Validation Engine)|
|TIMESTAMP CREAZIONE|Timestamp creazione della info run|
|API (\*DPM)|Rappresenta L’API utilizzata per il flusso DPM (non valorizzata per gli altri flussi)|
|IDENTIFICATIVO SOGGETTO ALIMENTANTE (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|TIPO ATTO (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|NUMERO ATTO (\*DPM)|Chiave flusso DPM (non valorizzata per gli altri flussi)|
|TIPO ESITO MDS (\*DPM)|Esito della response dell’API 2 (non valorizzata per gli altri flussi) |
|DATA RICEVUTA MDS (\*DPM)|Data della response dell’API 3 (non valorizzata per gli altri flussi)|
|CODICE REGIONE|Codice Regione del Mittente|
|ANNO RIFERIMENTO|Anno cui si riferiscono i dati del flusso|
|PERIODO DI RIFERIMENTO|Rappresenta il periodo di riferimento passato in input all’SDK|
|DESCRIZIONE STATO ESECUZIONE |Specifica il messaggio breve dell’errore, maggiori informazioni saranno presenti all’interno del log applicativo|
|NOME FILE OUTPUT MDS|Nome dei file di output inviati verso MdS|
|ESITO ACQUISIZIONE FLUSSO|Codice dell’esito del processo di acquisizione del flusso su MdS. Tale campo riflette la proprietà invioFlussiReturn/listaEsitiUpload/item/esito della response della procedura **invioFlussi**. (Es IF00)|
|CODICE ERRORE INVIO FLUSSI|Codice d’errore della procedura di invio. Tale campo riflette la proprietà InvioFlussiReturn/errorCode della response della procedura **invioFlussi**|
|TESTO ERRORE INVIO FLUSSI|Descrizione codice d’errore della procedura.Tale campo riflette la proprietà InvioFlussiReturn/ errorText della response della procedura **invioFlussi**|


Inoltre, a supporto dell’entità che rappresenta lo stato dell’esecuzione, sotto la cartella /sdk/log, saranno presenti anche i file di log applicativi (aggregati giornalmente) non strutturati, nei quali saranno presenti informazioni aggiuntive, ad esempio lo StackTrace (in caso di errori).

Il naming del file, se non modificata la politica di rolling (impostazioni), è il seguente:


**SDK \_AVT-VSM.log**

## mantainer:
 Accenture SpA until January 2026

