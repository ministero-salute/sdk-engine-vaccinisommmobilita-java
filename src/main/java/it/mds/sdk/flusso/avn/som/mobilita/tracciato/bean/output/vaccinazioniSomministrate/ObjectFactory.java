//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.06.16 alle 11:28:07 AM CEST 
//


/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.myschema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.myschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link VaccinazioniSomministrate }
     * 
     */
    public VaccinazioniSomministrate createVaccinazioniSomministrate() {
        return new VaccinazioniSomministrate();
    }

    /**
     * Create an instance of {@link VaccinazioniSomministrate.Assistito }
     * 
     */
    public VaccinazioniSomministrate.Assistito createVaccinazioniSomministrateAssistito() {
        return new VaccinazioniSomministrate.Assistito();
    }

    /**
     * Create an instance of {@link VaccinazioniSomministrate.Assistito.VaccinoSomministrato }
     * 
     */
    public VaccinazioniSomministrate.Assistito.VaccinoSomministrato createVaccinazioniSomministrateAssistitoVaccinoSomministrato() {
        return new VaccinazioniSomministrate.Assistito.VaccinoSomministrato();
    }

    /**
     * Create an instance of {@link VaccinazioniSomministrate.Assistito.VaccinoSomministrato.PrincipioVaccinale }
     * 
     */
    public VaccinazioniSomministrate.Assistito.VaccinoSomministrato.PrincipioVaccinale createVaccinazioniSomministrateAssistitoVaccinoSomministratoPrincipioVaccinale() {
        return new VaccinazioniSomministrate.Assistito.VaccinoSomministrato.PrincipioVaccinale();
    }

}
