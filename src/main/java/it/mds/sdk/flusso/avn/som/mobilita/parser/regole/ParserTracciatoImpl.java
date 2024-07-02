/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.parser.regole;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.CacheSQLite;
import it.mds.sdk.connettore.anagrafiche.tabella.EsitoBR3060;
import it.mds.sdk.connettore.anagrafiche.tabella.RecordBR3060;
import it.mds.sdk.connettore.anagrafiche.tabella.TabellaAnagrafica;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.conf.ConfigurazioneFlussoAvnSomMobilita;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.parser.ParserTracciato;
import it.mds.sdk.rest.exception.ParseCSVException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component("parserTracciatoAvnSomMobilita")
public class ParserTracciatoImpl implements ParserTracciato {

    private final int CHUNK_SIZE_BR3060 = 100000;
    private ConfigurazioneFlussoAvnSomMobilita conf = new ConfigurazioneFlussoAvnSomMobilita();

    /**
     * Il metodo converte un File.csv in una List<RecordDtoGenerico> usando come separatore "~"
     *
     * @param tracciato, File.csv di input
     * @return una lista di RecordDtoDir
     */
    @Override
    public List<RecordDtoGenerico> parseTracciato(File tracciato) {
        char separatore = conf.getSeparatore().getSeparatore().charAt(0);
        try (FileReader fileReader = new FileReader(tracciato)) {

            List<RecordDtoGenerico> dirList = new CsvToBeanBuilder<RecordDtoGenerico>(fileReader)
                    .withType(RecordDtoAvnSomMobilita.class)
                    .withSeparator(separatore)
                    .withSkipLines(1)   //Salta la prima riga del file CSV
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build()
                    .parse();
            fileReader.close();

            return dirList;

        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        } catch (Exception ex) {
            throw new ParseCSVException(ex.getMessage());
        }

        return Collections.emptyList();
    }


    public List<RecordDtoGenerico> parseTracciatoBlocco(File tracciato, int inizio, int fine) {
        StopWatch stopWatch = new StopWatch();
        char separatore = conf.getSeparatore().getSeparatore().charAt(0);
        log.info("Inizio lettura file {} da riga {} a riga {}", tracciato.getName(), inizio, fine);
        stopWatch.start();
        try (Reader reader = Files.newBufferedReader(tracciato.toPath())) {
            List<RecordDtoGenerico> res = new ArrayList<>();
            Iterator<RecordDtoAvnSomMobilita> it = new CsvToBeanBuilder<RecordDtoAvnSomMobilita>(reader)
                    .withType(RecordDtoAvnSomMobilita.class)
                    .withSeparator(separatore)
                    .withSkipLines(inizio + 1)   //Salta la prima riga del file CSV
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build()
                    .iterator();
            int count = inizio;
            int totaleRecordElaborati = 0;
            while (it.hasNext() && count < fine) {
                count++;
                RecordDtoGenerico recordGen = it.next();
                res.add(recordGen);
                totaleRecordElaborati++;
            }
            stopWatch.stop();
            log.info("Fine lettura file {} da riga {} a riga {} con {} record in {} ms", tracciato.getName(), inizio,
                    fine, totaleRecordElaborati, stopWatch.getLastTaskTimeMillis());

            return res;
        } catch (IOException e) {
            throw new ParseCSVException(e.getMessage());
        } catch (Exception ex) {
            throw new ParseCSVException(ex.getMessage());
        }
    }

    public List<EsitoBR3060> parseTracciatoBloccoBR3060(File tracciato) {

        log.info("**** BR3060 ****");
        log.info("Inizio parse tracciato file {} scrittura, filtraggio record e drop table", tracciato.getName());

        StopWatch stopWatch = new StopWatch();
        String nomeTabella = creaNomeTabellaDinamicoFromNomeRegola("BR3060");

        log.info("Inizio lettura file {} e scrittura nella tabella {}", tracciato.getName(), nomeTabella);

        stopWatch.start();

        try (Reader reader = Files.newBufferedReader(tracciato.toPath())) {

            Iterator<RecordDtoAvnSomMobilita> it = new CsvToBeanBuilder<RecordDtoAvnSomMobilita>(reader)
                    .withType(RecordDtoAvnSomMobilita.class)
                    .withSeparator('~')
                    .withSkipLines(1)   //Salta la prima riga del file CSV
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build()
                    .iterator();

            int count = 0;
            int idRecord = 0;
            List<RecordBR3060> list = new ArrayList<>();

            CacheSQLite cacheSQLite = CacheSQLite.getInstance();
            cacheSQLite.creaTabellaBR3060(nomeTabella);

            while (it.hasNext()) {
                count++;
                idRecord++;
                RecordDtoGenerico recordGen = it.next();
                RecordBR3060 recordBR3060 = createRecordBR3060FromRecordGenerico(recordGen, idRecord);

                list.add(recordBR3060);

                if (count == CHUNK_SIZE_BR3060 || !it.hasNext()) {
                    count = 0;
                    TabellaAnagrafica<RecordBR3060> tabellaAnagraficaBR3060 = new TabellaAnagrafica<>();
                    tabellaAnagraficaBR3060.setRecordsAnagrafica(list);
                    tabellaAnagraficaBR3060.setNome(nomeTabella);

                    cacheSQLite.addRecordBR3060(tabellaAnagraficaBR3060);
                    list.clear();
                }
            }

            log.info("Fine lettura file {} e scrittura nella tabella {}", tracciato.getName(), nomeTabella);
            log.info("Inizio filtraggio elementi scartati dalla tabella {}", nomeTabella);
            TabellaAnagrafica<EsitoBR3060> tabellaAnagraficaBR3060 = new TabellaAnagrafica<>();
            tabellaAnagraficaBR3060.setNome(nomeTabella);

            var listaScartati = cacheSQLite.selectFilterBR3060(tabellaAnagraficaBR3060);
            log.info("Fine filtraggio elementi scartati dalla tabella {}", nomeTabella);
            if (!cacheSQLite.dropTableBR3060(nomeTabella)) {
                log.debug("La Tabella {} non è stata droppata.", nomeTabella);
                throw new SQLException();
            }
            stopWatch.stop();
            log.info("Fine parse tracciato file {} scrittura, filtraggio record e drop table {} in {} ms", tracciato.getName(), nomeTabella, stopWatch.getLastTaskTimeMillis());
            log.info("***********");
            return listaScartati;

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ParseCSVException(ex.getMessage());
        }
    }

    private String creaNomeTabellaDinamicoFromNomeRegola(String nomeRegola) {

        String localDateTime = String.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        // Rimuoviamo i caratteri speciali, poichè la stringa costituirà il nome della tabella.
        localDateTime = localDateTime
                .replace(":", "")
                .replace("-", "")
                .replace(".", "");

        return nomeRegola + "_VSM_" + localDateTime;
    }

    private RecordBR3060 createRecordBR3060FromRecordGenerico(RecordDtoGenerico recordGen, int idRecord) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.debug("{}.createRecordBR3060FromRecordGenerico - Mapping record {} ", this.getClass().getName(), idRecord);
        return new RecordBR3060(
                idRecord,
                (String) recordGen.getCampo("tipoTrasmissione"),
                (String) recordGen.getCampo("codRegione"),
                (String) recordGen.getCampo("idAssistito"),
                (String) recordGen.getCampo("codTipoFormulazione"),
                (String) recordGen.getCampo("dataSomministrazione"),
                (String) recordGen.getCampo("codiceAntigene"),
                (Integer) recordGen.getCampo("dose")
        );
    }
}
