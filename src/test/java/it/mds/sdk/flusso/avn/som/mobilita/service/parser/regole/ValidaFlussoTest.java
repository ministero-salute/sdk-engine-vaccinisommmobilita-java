/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.service.parser.regole;

import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.ParserRegoleImpl;
import it.mds.sdk.flusso.avn.som.mobilita.parser.regole.ParserTracciatoImpl;
import it.mds.sdk.libreriaregole.parser.ParserRegole;
import it.mds.sdk.libreriaregole.parser.ParserTracciato;
import it.mds.sdk.libreriaregole.regole.beans.RegoleFlusso;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ValidaFlussoTest {
    private static final String FILE_REGOLE = "regole-avn-som-mob.xml";

    @Test
    void validaFlusso() throws JAXBException {
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        File regoleXML = new File(absolutePath + FileSystems.getDefault().getSeparator() + FILE_REGOLE);
        ParserRegole parserRegole = new ParserRegoleImpl();
        try {
            jakarta.xml.bind.JAXBContext jaxbContext = JAXBContextFactory
                    .createContext(new Class[]{RegoleFlusso.class}, null);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(parserRegole.parseRegole(new File("non/esisto")), new File(regoleXML.getPath()));
        } catch (JAXBException e) {
        }

    }

    @Test
    void parseRegole() throws JAXBException {
        ParserTracciato parserTracciato = new ParserTracciatoImpl();
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        File regoleXML = new File(absolutePath + FileSystems.getDefault().getSeparator() + FILE_REGOLE);
        ParserRegole parserRegole = new ParserRegoleImpl();
        RegoleFlusso regoleFlusso = parserRegole.parseRegole(regoleXML);

        try {
            jakarta.xml.bind.JAXBContext jaxbContext = JAXBContextFactory
                    .createContext(new Class[]{RegoleFlusso.class}, null);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(regoleFlusso, new File(regoleXML.getPath()));
        } catch (JAXBException e) {
        }
        System.out.println("Parse regole finito!");
    }
}
