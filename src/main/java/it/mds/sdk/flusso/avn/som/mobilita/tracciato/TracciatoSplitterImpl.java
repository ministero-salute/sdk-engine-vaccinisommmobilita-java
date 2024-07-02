/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.tracciato;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.RecordDtoAvnSomMobilita;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.conf.ConfigurazioneFlussoAvnSomMobilita;
import it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.ObjectFactory;
import it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.VaccinazioniSomministrate;
import it.mds.sdk.gestorefile.GestoreFile;
import it.mds.sdk.gestorefile.factory.GestoreFileFactory;
import it.mds.sdk.libreriaregole.tracciato.TracciatoSplitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component("tracciatoSplitterAvnSomMob")
public class TracciatoSplitterImpl implements TracciatoSplitter<RecordDtoAvnSomMobilita> {

    public static byte[] getBeansAsByteArray(final List<RecordDtoAvnSomMobilita> beans) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
        CSVWriter writer = new CSVWriter(streamWriter);

        StatefulBeanToCsv<RecordDtoAvnSomMobilita> beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
        try {
            beanToCsv.write(beans);
            streamWriter.flush();
        } catch (CsvDataTypeMismatchException | IOException | CsvRequiredFieldEmptyException e) {
            log.debug(e.getMessage(), e);
        }


        return stream.toByteArray();
    }

    //TODO eliminare
    @Override
    public List<Path> dividiTracciato(Path tracciato) {
        return null;
    }

    @Override
    public List<Path> dividiTracciato(List<RecordDtoAvnSomMobilita> records, String idRun) {
        try {
            ConfigurazioneFlussoAvnSomMobilita conf = new ConfigurazioneFlussoAvnSomMobilita();

            //Imposto gli attribute element
            String modalita = records.get(0).getModalita();
            String codiceRegione = records.get(0).getCodiceRegioneSomministrazione();

            //XML VACCINAZIONI
            ObjectFactory objAnagVaccSomm = new ObjectFactory();
            VaccinazioniSomministrate vaccinazioniSomministrate = objAnagVaccSomm.createVaccinazioniSomministrate();
            vaccinazioniSomministrate.setCodiceRegione(codiceRegione);
            vaccinazioniSomministrate.setModalita(
                    it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.Modalita.fromValue(modalita));

            for (RecordDtoAvnSomMobilita r : records) {
                if (!r.getTipoTrasmissione().equalsIgnoreCase("NM")) {
                    creaVaccinazioniXml(r, vaccinazioniSomministrate, objAnagVaccSomm);
                }
            }

            GestoreFile gestoreFile = GestoreFileFactory.getGestoreFile("XML");

            //recupero il path del file xsd di vaccinazioni
            URL resourceVaccinazioniXsd = this.getClass().getClassLoader().getResource("VSM.xsd");
            log.debug("URL dell'XSD per la validazione idrun {} : {}", idRun, resourceVaccinazioniXsd);
            //String pathXsdVacc = String.valueOf(Paths.get(resourceVaccinazioni.toURI()));

            //scrivi XML VACCINAZIONI
            String pathVaccinazioni = conf.getXmlOutput().getPercorso() + "SDK_AVT_VSM_" + records.get(0).getCampiInput().getPeriodoRiferimentoInput() + "_" + idRun + ".xml";
            //gestoreFile.scriviDto(vaccinazioniSomministrate, pathVaccinazioni, resourceVaccinazioniXsd);

            return List.of(Path.of(pathVaccinazioni));
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            log.error("[{}].dividiTracciato  - records[{}]  - idRun[{}] -" + e.getMessage(),
                    this.getClass().getName(),
                    e
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossibile validare il csv in ingresso. message: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private VaccinazioniSomministrate.Assistito creaAssistito(String idAssistito,
                                                              it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.ObjectFactory objectFactory) {
        VaccinazioniSomministrate.Assistito assistito = objectFactory.createVaccinazioniSomministrateAssistito();
        assistito.setIdAssistito(idAssistito);
        return assistito;
    }

    private void creaVaccinazioniXml(RecordDtoAvnSomMobilita r, VaccinazioniSomministrate vaccinazioniSomministrate,
                                     it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.ObjectFactory objAnag) {

        //ASSISTITO
        VaccinazioniSomministrate.Assistito currentAssistito = vaccinazioniSomministrate.getAssistito()
                .stream()
                .filter(assistito -> r.getIdAssistito().equalsIgnoreCase(assistito.getIdAssistito()))
                .findFirst()
                .orElse(null);

        if (currentAssistito == null) {
            currentAssistito = creaAssistito(r.getIdAssistito(), objAnag);
            vaccinazioniSomministrate.getAssistito().add(currentAssistito);

        }

        VaccinazioniSomministrate.Assistito.VaccinoSomministrato vaccinoSomministrato = creaAssistitoVaccinazione(r, objAnag);
        currentAssistito.getVaccinoSomministrato().add(vaccinoSomministrato);

    }

    private VaccinazioniSomministrate.Assistito.VaccinoSomministrato creaAssistitoVaccinazione(RecordDtoAvnSomMobilita r,
                                                                                               it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.ObjectFactory objectFactory) {
        XMLGregorianCalendar dataScadenza = null;
        XMLGregorianCalendar dataSomministrazione = null;
        VaccinazioniSomministrate.Assistito.VaccinoSomministrato vaccinoSomministrato = objectFactory.createVaccinazioniSomministrateAssistitoVaccinoSomministrato();
        vaccinoSomministrato.setTipoTrasmissione(r.getTipoTrasmissione());
        vaccinoSomministrato.setTipoErogatore(r.getTipoErogatore());
        vaccinoSomministrato.setCodiceStruttura(r.getCodStruttura());
        vaccinoSomministrato.setCodCondizioneSanitaria(r.getCodCondizioneSanitaria());
        vaccinoSomministrato.setCodCategoriaRischio(r.getCodCategoriaRischio());
        vaccinoSomministrato.setCodiceAICVaccino(r.getCodiceAICVaccino());
        vaccinoSomministrato.setDenomVaccino(r.getDenomVaccino());
        vaccinoSomministrato.setCodTipoFormulazione(r.getCodTipoFormulazione());
        vaccinoSomministrato.setViaSomministrazione(r.getViaSomministrazione());
        try {
            dataScadenza = r.getDataScadenza() != null ? DatatypeFactory.newInstance().newXMLGregorianCalendar(r.getDataScadenza()) : null;
            dataSomministrazione = r.getDataSomministrazione() != null ? DatatypeFactory.newInstance().newXMLGregorianCalendar(r.getDataSomministrazione()) : null;
            //dataDecesso = r.getDataDecesso() != null ? DatatypeFactory.newInstance().newXMLGregorianCalendar(r.getDataDecesso()) : null;
        } catch (DatatypeConfigurationException e) {
            log.debug(e.getMessage(), e);
        }
        vaccinoSomministrato.setDataScadenza(dataScadenza);
        vaccinoSomministrato.setModalitaPagamento(r.getModalitaPagamento());
        vaccinoSomministrato.setDataSomministrazione(dataSomministrazione);
        vaccinoSomministrato.setSitoInoculazione(r.getSitoInoculazione());
        vaccinoSomministrato.setComuneSomministrazione(r.getCodiceComuneSomministrazione());
        vaccinoSomministrato.setAslSomministrazione(r.getCodiceAslSomministrazione());
        vaccinoSomministrato.setRegioneSomministrazione(r.getCodiceRegioneSomministrazione());
        vaccinoSomministrato.setStatoEsteroSomministrazione(r.getStatoEsteroSomministrazione());
        VaccinazioniSomministrate.Assistito.VaccinoSomministrato.PrincipioVaccinale principioVaccinale = new VaccinazioniSomministrate.Assistito.VaccinoSomministrato.PrincipioVaccinale();
        principioVaccinale.setCodAntigene(r.getCodiceAntigene());
        principioVaccinale.setDose(r.getDose() != null ? BigInteger.valueOf(r.getDose()) : null);
        vaccinoSomministrato.setPrincipioVaccinale(List.of(principioVaccinale));
        vaccinoSomministrato.setLottoVaccino(r.getLottoVaccino());
//        VaccinazioniSomministrate.Assistito assistito = objectFactory.createVaccinazioniSomministrateAssistito();
//        assistito.setIdAssistito(r.getIdAssistito());
//        assistito.setVaccinoSomministrato(List.of(vaccinoSomministrato));
        return vaccinoSomministrato;
    }

    public VaccinazioniSomministrate creaVaccinazioniSomministrate(List<RecordDtoAvnSomMobilita> records, VaccinazioniSomministrate vaccinazioniSomministrate) {

        //Imposto gli attribute element
        String modalita = records.get(0).getModalita();
        String codiceRegione = records.get(0).getCodRegione();

        if (vaccinazioniSomministrate == null) {
            ObjectFactory objAnagVaccSomm = new ObjectFactory();
            vaccinazioniSomministrate = objAnagVaccSomm.createVaccinazioniSomministrate();
            vaccinazioniSomministrate.setCodiceRegione(codiceRegione);
            vaccinazioniSomministrate.setModalita(
                    it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.Modalita.fromValue(modalita));

            for (RecordDtoAvnSomMobilita r : records) {
                if (!"NM".equalsIgnoreCase(r.getTipoTrasmissione())) {
                    creaVaccinazioniXml(r, vaccinazioniSomministrate, objAnagVaccSomm);
                }
            }

        }
        return vaccinazioniSomministrate;
    }

}
