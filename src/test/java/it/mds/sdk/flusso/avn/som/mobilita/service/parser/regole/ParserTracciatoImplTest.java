/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.service.parser.regole;

import it.mds.sdk.connettore.anagrafiche.gestore.anagrafica.CacheSQLite;
import it.mds.sdk.connettore.anagrafiche.sqlite.QueryAnagrafica;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.ParserTracciatoImpl;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.RecordDtoAvnSomMobilita;
import it.mds.sdk.flusso.avn.som.mobilita.tracciato.TracciatoSplitterImpl;
import it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate.VaccinazioniSomministrate;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.parser.ParserTracciato;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ParserTracciatoImplTest {
    private static final String FILE_TRACCIATO_VSX = "VSM.csv";

    MockedStatic<CacheSQLite> cacheSQLiteMockedStatic;

    MockedStatic<QueryAnagrafica> queryAnagraficaMockedStatic;

    QueryAnagrafica queryAnagrafica = Mockito.mock(QueryAnagrafica.class);

    CacheSQLite cacheSQLite = new CacheSQLite();

    private int dimensioneBlocco = 300;
    String pathFileCsv;
    String pathFileCsv1000;
    File fileCsv;

    File fileCsv1000;

    @BeforeEach
    void init() {
        queryAnagraficaMockedStatic = mockStatic(QueryAnagrafica.class);
        cacheSQLiteMockedStatic = mockStatic(CacheSQLite.class);
        Properties prop = loadPropertiesFromFile("config-flusso-avn-som-mobilita-test.properties");
        this.pathFileCsv = prop.getProperty("test.filecsv");
        this.pathFileCsv1000 = prop.getProperty("test.filecsv1000");

        ClassLoader classLoader = getClass().getClassLoader();
        fileCsv = new File(Objects.requireNonNull(classLoader.getResource(pathFileCsv)).getFile());
        fileCsv1000 = new File(Objects.requireNonNull(classLoader.getResource(pathFileCsv1000)).getFile());
        System.out.println("ciao");
    }

    @Test
    void getInstanceTest() {
        queryAnagraficaMockedStatic.when(CacheSQLite::getInstance).thenReturn(cacheSQLite);
        CacheSQLite.getInstance();
    }

    @Test
    void integBLocco() {
        ParserTracciatoImpl p = new ParserTracciatoImpl();
        List<RecordDtoGenerico> list = p.parseTracciatoBlocco(fileCsv, 1, dimensioneBlocco);
        CampiInputBean c = new CampiInputBean("13", "2022", "120");

        for (RecordDtoGenerico r : list) {
            r.setCampiInput(c);
        }
        List<RecordDtoAvnSomMobilita> r2 = list.stream().map(k -> (RecordDtoAvnSomMobilita) k).collect(Collectors.toList());
        TracciatoSplitterImpl splitter = new TracciatoSplitterImpl();
        splitter.dividiTracciato(r2, "34");
        System.out.println(list.size());

    }

    @Test
    void scritturaNBlocchiSuXML() {
        //TODO voglio provare a marshallare un intero file XML con tutti i crismi
        ParserTracciatoImpl p = new ParserTracciatoImpl();

        //Prova blocchi
        List<RecordDtoGenerico> blocco = p.parseTracciatoBlocco(fileCsv1000, 1, dimensioneBlocco);
        List<RecordDtoAvnSomMobilita> bloccoConv = blocco.stream().map(k -> (RecordDtoAvnSomMobilita) k).collect(Collectors.toList());

        TracciatoSplitterImpl t = new TracciatoSplitterImpl();
        VaccinazioniSomministrate vaccinazioniSomministrate = t.creaVaccinazioniSomministrate(bloccoConv, null);
        blocco = null;
        bloccoConv = null;
        //System.gc();
        System.out.println("blocco 1");
        blocco = p.parseTracciatoBlocco(fileCsv1000, dimensioneBlocco + 1, dimensioneBlocco + 300);
        bloccoConv = blocco.stream().map(k -> (RecordDtoAvnSomMobilita) k).collect(Collectors.toList());
        vaccinazioniSomministrate = t.creaVaccinazioniSomministrate(bloccoConv, vaccinazioniSomministrate);
    }

    private Properties loadPropertiesFromFile(String fileName) {
        Properties prop = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(fileName);
            prop.load(stream);
            stream.close();
        } catch (Exception e) {
            String msg = String.format("Failed to load file '%s' - %s - %s", fileName, e.getClass().getName(),
                    e.getMessage());
            System.out.println(msg);
        }
        return prop;
    }

    @Test
    void validaTracciatoOK() {
        ParserTracciato parserTracciato = new ParserTracciatoImpl();
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        File tracciato = new File(absolutePath + FileSystems.getDefault().getSeparator() + FILE_TRACCIATO_VSX);

        List<RecordDtoGenerico> listaRecord = parserTracciato.parseTracciato(tracciato);
        listaRecord.forEach(System.out::println);
        assertFalse(ArrayUtils.isEmpty(listaRecord.toArray()));
    }

    @Test
    void validaTracciatoBloccoBR3060() throws SQLException {
        ParserTracciatoImpl parserTracciato = new ParserTracciatoImpl();
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        File tracciato = new File(absolutePath + FileSystems.getDefault().getSeparator() + FILE_TRACCIATO_VSX);
        cacheSQLiteMockedStatic.when(CacheSQLite::getInstance).thenReturn(cacheSQLite);
        queryAnagraficaMockedStatic.when(QueryAnagrafica::getInstanceWithCache).thenReturn(queryAnagrafica);
        Mockito.when(queryAnagrafica.creaTabellaBR3060(any())).thenReturn(Boolean.TRUE);
        Mockito.when(queryAnagrafica.dropTable(any())).thenReturn(Boolean.TRUE);
        parserTracciato.parseTracciatoBloccoBR3060(tracciato);
    }

    @AfterEach
    void closeStaticMocks() {
        queryAnagraficaMockedStatic.close();
        cacheSQLiteMockedStatic.close();
    }
}
