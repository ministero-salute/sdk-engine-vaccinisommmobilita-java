/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.service;


import it.mds.sdk.connettore.anagrafiche.tabella.EsitoBR3060;
import it.mds.sdk.connettoremds.ConnettoreMds;
import it.mds.sdk.connettoremds.crittografia.Crittografia;
import it.mds.sdk.connettoremds.exception.ConnettoreMdsException;
import it.mds.sdk.connettoremds.exception.CrittografiaException;
import it.mds.sdk.connettoremds.gaf.webservices.bean.ArrayOfUploadEsito;
import it.mds.sdk.connettoremds.gaf.webservices.bean.ResponseUploadFile;
import it.mds.sdk.connettoremds.gaf.webservices.bean.UploadEsito;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.ParserTracciatoImpl;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.RecordDtoAvnSomMobilita;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.conf.ConfigurazioneFlussoAvnSomMobilita;
import it.mds.sdk.flusso.avn.som.mobilita.tracciato.TracciatoSplitterImpl;
import it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.VaccinazioniSomministrate;
import it.mds.sdk.gestoreesiti.GestoreRunLog;
import it.mds.sdk.gestoreesiti.conf.Configurazione;
import it.mds.sdk.gestoreesiti.modelli.InfoRun;
import it.mds.sdk.gestoreesiti.modelli.ModalitaOperativa;
import it.mds.sdk.gestoreesiti.modelli.StatoRun;
import it.mds.sdk.gestoreesiti.validazioneXSD.MainTester;
import it.mds.sdk.gestorefile.GestoreFile;
import it.mds.sdk.gestorefile.exception.XSDNonSupportedException;
import it.mds.sdk.gestorefile.factory.GestoreFileFactory;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.gestorevalidazione.BloccoValidazione;
import it.mds.sdk.libreriaregole.parser.ParserRegole;
import it.mds.sdk.libreriaregole.parser.ParserTracciato;
import it.mds.sdk.libreriaregole.regole.beans.RegoleFlusso;
import it.mds.sdk.libreriaregole.tracciato.TracciatoSplitter;
import it.mds.sdk.libreriaregole.validator.ValidationEngine;
import it.mds.sdk.rest.exception.ParseCSVException;
import it.mds.sdk.rest.persistence.entity.FlussoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@Component("flussoAvnSomMobilitaService")
public class FlussoAvnSomMobilitaService {
    private static final String MDS_RESP_OK = "IF00";
    private static final String TAG_FLUSSO_XML = "</vaccinazioniSomministrate><vaccinazioniSomministrate CodiceRegione=";
    private ParserRegole parserRegole;
    private ParserTracciato parserTracciato;
    private ValidationEngine validationEngine;
    private TracciatoSplitter<RecordDtoAvnSomMobilita> tracciatoSplitter;
    private ConnettoreMds connettoreMds;
    private ConfigurazioneFlussoAvnSomMobilita conf;
    private Crittografia crittografia;
    private final String CATEGORIA_FLUSSI = "AVT";

    private final String NOME_FILE_XSD_AVN = "VSM.xsd";
    private ModalitaOperativa ModalitaOperativa;
    private final Configurazione config = new Configurazione();

    @Autowired
    public FlussoAvnSomMobilitaService(@Qualifier("parserRegoleAvnSomMobilita") final ParserRegole parserRegole,
                                       @Qualifier("parserTracciatoAvnSomMobilita") final ParserTracciato parserTracciato,
                                       @Qualifier("validationEngine") final ValidationEngine validationEngine,
                                       @Qualifier("tracciatoSplitterAvnSomMob") final TracciatoSplitter<RecordDtoAvnSomMobilita> tracciatoSplitter,
                                       @Qualifier("connettoreMds") final ConnettoreMds connettoreMds,
                                       @Qualifier("configurazioneFlussoAvnSomMobilita") final ConfigurazioneFlussoAvnSomMobilita conf,
                                       final Crittografia crittografia
    ) {
        this.parserRegole = parserRegole;
        this.parserTracciato = parserTracciato;
        this.validationEngine = validationEngine;
        this.tracciatoSplitter = tracciatoSplitter;
        this.connettoreMds = connettoreMds;
        this.conf = conf;
        this.crittografia = crittografia;
    }

    public void validazioneBlocchi(int dimensioneBlocco, String nomeFile, RegoleFlusso regoleFlusso, String idRun,
                                   String idClient, ModalitaOperativa modalitaOperativa, String periodoRiferimento,
                                   String annoRiferimento, String codiceRegione, GestoreRunLog gestoreRunLog, List<EsitoBR3060> listaEsitiBR3060) {
        //Loop
        List<RecordDtoGenerico> records;
        int inizio = 0;
        int fine = dimensioneBlocco;
        ParserTracciatoImpl parser = getParserTracciatoImpl();
        CampiInputBean campiInputBean = CampiInputBean.builder()
                .withAnnoRiferimentoInput(annoRiferimento)
                .withCodiceRegioneInput(codiceRegione)
                .withPeriodoRiferimentoInput(periodoRiferimento)
                .build();
        //Legge un blocco dal CSV e scrive records, su questo blocco chiama il valida flusso
        VaccinazioniSomministrate vaccinazioniSomministrate = null;
        File file = new File(conf.getFlusso().getPercorso() + nomeFile);
        String nomeFileXml = conf.getXmlOutput().getPercorso() + "SDK_AVT_VSM_" + periodoRiferimento + "_" + idRun + ".xml";

        String nomeFileXmlTmp = nomeFileXml + "tmp";
        int totale = 0;
        int accettati = 0;
        int scartati = 0;
        int numeroBlocco = 0;
        int numRecordValidati = 0;
        final String percorso = String.format("%s/ESITO_%s.json", config.getEsito().getPercorso(), idRun);
        final String percorsoTemp = String.format("%s/ESITO_%s_TEMP.json", config.getEsito().getPercorso(), idRun);
        do {
            numeroBlocco++;
            log.info("Inizio elaborazione blocco {} con dimensione massima {} di idRun {}", numeroBlocco,
                    dimensioneBlocco, idRun);
            try {
                records = parser.parseTracciatoBlocco(file, inizio, fine);
            } catch (ParseCSVException pe) {
                log.error("Errore di parsing del blocco {} idRun {}", numeroBlocco, idRun, pe);
                InfoRun infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_GENERICO);
                infoRun.setDescrizioneStatoEsecuzione(pe.getMessage());
                infoRun.setDataFineEsecuzione(new Timestamp(System.currentTimeMillis()));
                gestoreRunLog.updateRun(infoRun);
                return;
            }
            inizio = inizio + dimensioneBlocco;
            fine = fine + records.size();
            records.forEach(r -> r.setCampiInput(campiInputBean));

            BloccoValidazione bloccoValidazione = validationEngine.validaFlussoBloccoConRegoleInteroFlusso(records, regoleFlusso, idRun, getIdRecordsScartati(listaEsitiBR3060), numRecordValidati);

            // fix numero record. ovvero se non facessi questo controllo, la validazione riporterebbe record con lo stesso idRecord e romperebbe inolte la BR3060.
            numRecordValidati += records.size();

            totale = totale + bloccoValidazione.getNumeroRecord();
            scartati = scartati + bloccoValidazione.getScartati();
            accettati = accettati + bloccoValidazione.getNumeroRecord() - bloccoValidazione.getScartati();
            //scrittura esito parziale o append
            validationEngine.creaFileEsiti(bloccoValidazione);
            //Scrittura XML solo se almeno un record è presente
            List<RecordDtoAvnSomMobilita> recordConv =
                    bloccoValidazione.getRecordList().stream().map(k -> (RecordDtoAvnSomMobilita) k).collect(Collectors.toList());
            boolean isWriteOk = true;
            if (!recordConv.isEmpty()) {
                log.debug("Per il blocco {} di idRun {} presenti {} record da trasformare in xml", numeroBlocco,
                        idRun, recordConv.size());
                vaccinazioniSomministrate = ((TracciatoSplitterImpl) tracciatoSplitter).creaVaccinazioniSomministrate(recordConv, vaccinazioniSomministrate);
            } else {
                log.warn("Nessun record valido per il blocco {} di idRun {}, non viene generato xml", numeroBlocco, idRun);
                isWriteOk = false;
            }

            //creo xml solo se sono presenti record accettati
            if (isWriteOk) {
                GestoreFile gestoreFile = GestoreFileFactory.getGestoreFile("XML");
                URL urlXsd = this.getClass().getClassLoader().getResource(NOME_FILE_XSD_AVN);
                log.debug("URL dell'XSD per la validazione idrun {} : {}", idRun, urlXsd);
                log.info("Inizio scrittura file temporaneo {} con xsd {} per idRun {}", nomeFileXmlTmp,
                        urlXsd.getFile(), idRun);
                try {
                    gestoreFile.scriviDtoFragment(vaccinazioniSomministrate, nomeFileXmlTmp, urlXsd);
                } catch (XSDNonSupportedException e) {
                    log.error("XSD non validato. ", e);
                    InfoRun infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_VALIDAZIONE_SDK);
                    infoRun.setDescrizioneStatoEsecuzione(e.getMessage());
                    infoRun.setDataFineEsecuzione(new Timestamp(System.currentTimeMillis()));
                    gestoreRunLog.updateRun(infoRun);
                    boolean isFileEsitiCleaned = validationEngine.formatJsonEsiti(percorso, percorsoTemp);
                    if (!isFileEsitiCleaned) {
                        log.warn("ATTENZIONE: Un'operazione tra copia, rename o eliminazione del file temp degli esiti non è andata a buon fine.");
                        throw new RuntimeException();
                    }
                    return;
                }
                log.info("Scritto file temporaneo {} per idRun {}", nomeFileXmlTmp, idRun);
                vaccinazioniSomministrate = null;
                System.gc();
            }

            log.info("Terminata elaborazione blocco {} di idRun {} con questi risultati:\n" +
                            "Record elaborati: {}\nRecord accettati: {}\nRecord scartati: {}\n" +
                            "Totale dei blocchi elaborati fino ad ora:\nRecord elaborati: {}\nRecord accettati: {}\nRecord " +
                            "scartati: {}", numeroBlocco, idRun, bloccoValidazione.getNumeroRecord(),
                    bloccoValidazione.getNumeroRecord() - bloccoValidazione.getScartati(), bloccoValidazione.getScartati(), totale
                    , accettati, scartati);
        } while (fine - inizio >= dimensioneBlocco);

        //TODO rendere il file XML utilizzabile

        boolean isFileEsitiCleaned = validationEngine.formatJsonEsiti(percorso, percorsoTemp);

        if (!isFileEsitiCleaned) {
            log.warn("ATTENZIONE: Un'operazione tra copia, rename o eliminazione del file temp degli esiti non è andata a buon fine.");
            throw new RuntimeException();
        }

        InfoRun infoRun = gestoreRunLog.getRun(idRun);

        if (accettati > 0) {
            log.info("Inizio pulizia file {}", nomeFileXmlTmp);
            String nomeFilePulito;
            try {
                nomeFilePulito = validationEngine.puliziaFileAvn(nomeFileXmlTmp, nomeFileXml, TAG_FLUSSO_XML);
                boolean isFileXMLValidated = validationEngine.validateXML(nomeFilePulito, NOME_FILE_XSD_AVN);
                if (!isFileXMLValidated) {
                    throw new XSDNonSupportedException();
                }
            } catch (XSDNonSupportedException x) {
                log.error("XSD non validato.", x);
                infoRun.setStatoEsecuzione(StatoRun.KO_VALIDAZIONE_SDK);
                infoRun.setDescrizioneStatoEsecuzione(x.getMessage());
                infoRun.setDataFineEsecuzione(new Timestamp(System.currentTimeMillis()));
                gestoreRunLog.updateRun(infoRun);
                return;
            }
            log.info("Pulito file {}", nomeFilePulito);
        }
        infoRun.setNumeroRecord(totale);
        infoRun.setNumeroRecordScartati(scartati);
        infoRun.setNumeroRecordAccettati(accettati);
        infoRun.setDataFineEsecuzione(new Timestamp(System.currentTimeMillis()));
        infoRun = gestoreRunLog.updateRun(infoRun);

        //Update inforun ad elaborata se modalitàOperativa = T
        if (modalitaOperativa == modalitaOperativa.T) {
            log.debug("modalità operativa T, idRun {} in stato elaborata", idRun);
            infoRun.setStatoEsecuzione(StatoRun.ELABORATA);
            gestoreRunLog.updateRun(infoRun);
            return;
        }
        String soglia = conf.getSogliaInvio().getSoglia();
        var divisiore = new BigDecimal(totale);
        BigDecimal sogliaCalcolata =
                new BigDecimal(accettati).divide(divisiore, 1, RoundingMode.FLOOR).multiply(new BigDecimal("100"));
        log.info("Soglia calcolata {}, soglia fissata {}", sogliaCalcolata, soglia);

        List<Path> listPath = List.of(Path.of(nomeFileXml));
        if (accettati > 0 && (sogliaCalcolata.compareTo(new BigDecimal(soglia)) >= 0) && modalitaOperativa.equals(ModalitaOperativa.P)) {
            log.debug("Soglia " + soglia + " raggiunta e modalita P");
            log.debug("Inizio crittografia dei file {}", listPath);
            List<Path> pathCriptatiList = new ArrayList<>();
            Path cert = Paths.get(conf.getCertificato().getPercorsoCertificato());
            Path fileCriptato = Path.of("");
            for (Path fileCripta : listPath) {

                try {
                    fileCriptato = crittografia.criptaFile(fileCripta, cert);
                } catch (CrittografiaException e) {
                    log.error("Errore crittografia file {}, non viene effettuato nessun invio verso MdS",
                            fileCripta.getFileName(), e);
                    infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_INVIO_MINISTERO);
                    infoRun.setDescrizioneStatoEsecuzione(e.getMessage());
                    gestoreRunLog.updateRun(infoRun);
                    return;
                } catch (Throwable t) {
                    infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_GENERICO);
                    log.error("Errore generico idRun {}", idRun, t);
                    infoRun.setDescrizioneStatoEsecuzione(t.getMessage());
                    gestoreRunLog.updateRun(infoRun);
                    return;
                }

                pathCriptatiList.add(fileCriptato);
            }
            log.debug("Lista file criptati: {}", pathCriptatiList);
            listPath = pathCriptatiList;

            infoRun.setNomeFileOutputMds(listPath.toString());
            gestoreRunLog.updateRun(infoRun);
            String nomeFileXmlCriptato = conf.getXmlOutput().getPercorso() + fileCriptato.getFileName();
            this.inviaTracciatoMds(idRun, nomeFileXmlCriptato, gestoreRunLog, periodoRiferimento, annoRiferimento);
        } else if (accettati == 0) {
            log.warn("Record da mandare al MdS vuoti per idRun {}", idRun);
            infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_INVIO_SOGLIA);
            infoRun.setDescrizioneStatoEsecuzione("Soglia minima non raggiunta");
            gestoreRunLog.updateRun(infoRun);
        } else {
            log.warn("Soglia {} non raggiunta, è {}", soglia, sogliaCalcolata);
            infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_INVIO_SOGLIA);
            infoRun.setDescrizioneStatoEsecuzione("Soglia minima non raggiunta");
            gestoreRunLog.updateRun(infoRun);
        }
    }

    public ParserTracciatoImpl getParserTracciatoImpl() {
        return new ParserTracciatoImpl();
    }

    private List<String> getIdRecordsScartati(List<EsitoBR3060> listaEsitiBR3060) {
        List<String> listaScarti = new ArrayList<>();
        for (EsitoBR3060 e : listaEsitiBR3060) {
            listaScarti.addAll(Arrays.stream(e.getIds().split("\\|")).collect(Collectors.toList()));
        }
        return listaScarti;
    }

    public void inviaTracciatoMds(String idRun, String nomeFileXml, GestoreRunLog gestoreRunLog,
                                  String periodoRiferimento, String annoRiferimento) {
        InfoRun infoRun = gestoreRunLog.getRun(idRun);

        infoRun.setNomeFileOutputMds(nomeFileXml);
        gestoreRunLog.updateRun(infoRun);
        List<Path> listPath = List.of(Path.of(nomeFileXml));
        try {
            ResponseUploadFile resp = connettoreMds.invioTracciati(listPath, CATEGORIA_FLUSSI, "", periodoRiferimento, annoRiferimento);
            if (!resp.getErrorCode().isBlank()) {
                log.warn("errore presente in risposta mds per idRun {} : {}", idRun, resp.getErrorCode());
                infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_INVIO_MINISTERO);
            } else if (resp.getListaEsitiUpload().getItem().stream().filter(i -> !MDS_RESP_OK.equals(i.getEsito())).collect(Collectors.toList()).isEmpty()) {
                log.debug("Riposta MDS ok per run {}, cambio stato a {} e copia xml", idRun, StatoRun.ELABORATA);
                infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.ELABORATA);

                for (Path pathDaCopiare : listPath) {
                    Files.copy(pathDaCopiare,
                            Paths.get(conf.getSent().getPercorsoSent() + pathDaCopiare.getFileName().toString()));
                }
            } else {
                log.warn("Errore risposta MDS per idRun {}, cambio stato a {}", idRun, StatoRun.KO_INVIO_MINISTERO);
                infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_INVIO_MINISTERO);
            }
            infoRun.setCodiceErroreInvioFlusso(resp.getErrorCode());
            infoRun.setTestoErroreInvioFlusso(resp.getErrorText());
            if (resp.getErrorCode() == null || resp.getErrorCode().isBlank()) {
                ArrayOfUploadEsito listaEsitiResp = resp.getListaEsitiUpload();
                StringJoiner joiner = new StringJoiner("|");
                for (UploadEsito esitoFile : listaEsitiResp.getItem()) {
                    joiner.add(esitoFile.getNomeFile() + "-" + esitoFile.getEsito());
                }
                infoRun.setEsitoAcquisizioneFlusso(joiner.toString());
                List<String> listStringhe = listaEsitiResp.getItem().stream().map(UploadEsito::getIdUpload).collect(Collectors.toList());
                if (listStringhe != null && !listStringhe.isEmpty()) {
                    infoRun.setIdUploads(listStringhe);
                }
            }
            gestoreRunLog.updateRun(infoRun);
        } catch (ConnettoreMdsException | IOException e) {
            log.error("Errore invioFlussi al ministero per file {}", listPath);
            infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_INVIO_MINISTERO);
            infoRun.setDescrizioneStatoEsecuzione(e.getMessage());
            gestoreRunLog.updateRun(infoRun);
        } catch (Throwable t) {
            infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_GENERICO);
            log.error("Errore generico idRun {}", idRun, t);
            infoRun.setDescrizioneStatoEsecuzione(t.getMessage());
            gestoreRunLog.updateRun(infoRun);
        }
    }

    public List<EsitoBR3060> validazioneRegoleInteroFlusso(String nomeFile, String idRun, GestoreRunLog gestoreRunLog) {

        try {
            ParserTracciatoImpl parser = new ParserTracciatoImpl();

            File file = new File(conf.getFlusso().getPercorso() + nomeFile);
            if (!file.exists()) {
                log.warn("File regole {} non trovato ", conf.getRules().getPercorso());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File non trovato");
            }
            return parser.parseTracciatoBloccoBR3060(file);

        } catch (ParseCSVException pe) {
            log.error(pe.getMessage(), pe);
            InfoRun infoRun = gestoreRunLog.cambiaStatoRun(idRun, StatoRun.KO_GENERICO);
            infoRun.setDescrizioneStatoEsecuzione(pe.getMessage());
            infoRun.setDataFineEsecuzione(new Timestamp(System.currentTimeMillis()));
            gestoreRunLog.updateRun(infoRun);
            return List.of();
        }
    }

    public MainTester getMainTester() {
        return new MainTester();
    }
    @Async
    public void lanciaValidazioniPerFlusso(int dimensioneBlocco, FlussoRequest flusso, InfoRun infoRun, GestoreRunLog gestoreRunLog, RegoleFlusso regoleFlusso) {
        List<EsitoBR3060> listaEsitiBR3060 = this.validazioneRegoleInteroFlusso(flusso.getNomeFile(),  infoRun.getIdRun(), gestoreRunLog);
        this.validazioneBlocchi(dimensioneBlocco, flusso.getNomeFile(), regoleFlusso, infoRun.getIdRun(),
                flusso.getIdClient(), flusso.getModalitaOperativa(),
                flusso.getPeriodoRiferimento(), flusso.getAnnoRiferimento(),
                flusso.getCodiceRegione(), gestoreRunLog, listaEsitiBR3060);
    }
}
