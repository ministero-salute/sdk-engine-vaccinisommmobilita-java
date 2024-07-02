//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.06.16 alle 11:28:07 AM CEST 
//


/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.tracciato.bean.output.vaccinazioniSomministrate;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Assistito" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="VaccinoSomministrato" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="PrincipioVaccinale" maxOccurs="unbounded"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;attribute name="CodAntigene" use="required" type="{}CodAntigene" /&gt;
 *                                     &lt;attribute name="Dose" use="required" type="{}Dose" /&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="TipoTrasmissione" use="required" type="{}TipoTrasmissione" /&gt;
 *                           &lt;attribute name="TipoErogatore" use="required" type="{}TipoErogatore" /&gt;
 *                           &lt;attribute name="CodiceStruttura" type="{}CodiceStruttura" /&gt;
 *                           &lt;attribute name="CodCondizioneSanitaria" use="required" type="{}CodCondizioneSanitaria" /&gt;
 *                           &lt;attribute name="CodCategoriaRischio" use="required" type="{}CodCategoriaRischio" /&gt;
 *                           &lt;attribute name="CodiceAICVaccino" type="{}CodiceAICVaccino" /&gt;
 *                           &lt;attribute name="DenomVaccino" type="{}DenomVaccino" /&gt;
 *                           &lt;attribute name="CodTipoFormulazione" use="required" type="{}CodTipoFormulazione" /&gt;
 *                           &lt;attribute name="ViaSomministrazione" use="required" type="{}ViaSomministrazione" /&gt;
 *                           &lt;attribute name="LottoVaccino" type="{}LottoVaccino" /&gt;
 *                           &lt;attribute name="DataScadenza" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
 *                           &lt;attribute name="ModalitaPagamento" use="required" type="{}ModalitaPagamento" /&gt;
 *                           &lt;attribute name="DataSomministrazione" use="required" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
 *                           &lt;attribute name="SitoInoculazione" use="required" type="{}SitoInoculazione" /&gt;
 *                           &lt;attribute name="ComuneSomministrazione" type="{}ComuneSomministrazione" /&gt;
 *                           &lt;attribute name="AslSomministrazione" type="{}AslSomministrazione" /&gt;
 *                           &lt;attribute name="RegioneSomministrazione" type="{}RegioneSomministrazione" /&gt;
 *                           &lt;attribute name="StatoEsteroSomministrazione" type="{}StatoEsteroSomministrazione" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="IdAssistito" use="required" type="{}IdAssistito" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="CodiceRegione" use="required" type="{}CodiceRegione" /&gt;
 *       &lt;attribute name="Modalita" use="required" type="{}Modalita" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "assistito"
})
@XmlRootElement(name = "vaccinazioniSomministrate")
public class VaccinazioniSomministrate {

    @XmlElement(name = "Assistito", required = true)
    protected List<Assistito> assistito;
    @XmlAttribute(name = "CodiceRegione", required = true)
    protected String codiceRegione;
    @XmlAttribute(name = "Modalita", required = true)
    protected Modalita modalita;

    /**
     * Gets the value of the assistito property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the assistito property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssistito().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Assistito }
     * 
     * 
     */
    public List<Assistito> getAssistito() {
        if (assistito == null) {
            assistito = new ArrayList<Assistito>();
        }
        return this.assistito;
    }

    /**
     * Recupera il valore della proprietà codiceRegione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceRegione() {
        return codiceRegione;
    }

    /**
     * Imposta il valore della proprietà codiceRegione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceRegione(String value) {
        this.codiceRegione = value;
    }

    /**
     * Recupera il valore della proprietà modalita.
     * 
     * @return
     *     possible object is
     *     {@link Modalita }
     *     
     */
    public Modalita getModalita() {
        return modalita;
    }

    /**
     * Imposta il valore della proprietà modalita.
     * 
     * @param value
     *     allowed object is
     *     {@link Modalita }
     *     
     */
    public void setModalita(Modalita value) {
        this.modalita = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="VaccinoSomministrato" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="PrincipioVaccinale" maxOccurs="unbounded"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;attribute name="CodAntigene" use="required" type="{}CodAntigene" /&gt;
     *                           &lt;attribute name="Dose" use="required" type="{}Dose" /&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *                 &lt;attribute name="TipoTrasmissione" use="required" type="{}TipoTrasmissione" /&gt;
     *                 &lt;attribute name="TipoErogatore" use="required" type="{}TipoErogatore" /&gt;
     *                 &lt;attribute name="CodiceStruttura" type="{}CodiceStruttura" /&gt;
     *                 &lt;attribute name="CodCondizioneSanitaria" use="required" type="{}CodCondizioneSanitaria" /&gt;
     *                 &lt;attribute name="CodCategoriaRischio" use="required" type="{}CodCategoriaRischio" /&gt;
     *                 &lt;attribute name="CodiceAICVaccino" type="{}CodiceAICVaccino" /&gt;
     *                 &lt;attribute name="DenomVaccino" type="{}DenomVaccino" /&gt;
     *                 &lt;attribute name="CodTipoFormulazione" use="required" type="{}CodTipoFormulazione" /&gt;
     *                 &lt;attribute name="ViaSomministrazione" use="required" type="{}ViaSomministrazione" /&gt;
     *                 &lt;attribute name="LottoVaccino" type="{}LottoVaccino" /&gt;
     *                 &lt;attribute name="DataScadenza" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
     *                 &lt;attribute name="ModalitaPagamento" use="required" type="{}ModalitaPagamento" /&gt;
     *                 &lt;attribute name="DataSomministrazione" use="required" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
     *                 &lt;attribute name="SitoInoculazione" use="required" type="{}SitoInoculazione" /&gt;
     *                 &lt;attribute name="ComuneSomministrazione" type="{}ComuneSomministrazione" /&gt;
     *                 &lt;attribute name="AslSomministrazione" type="{}AslSomministrazione" /&gt;
     *                 &lt;attribute name="RegioneSomministrazione" type="{}RegioneSomministrazione" /&gt;
     *                 &lt;attribute name="StatoEsteroSomministrazione" type="{}StatoEsteroSomministrazione" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="IdAssistito" use="required" type="{}IdAssistito" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "vaccinoSomministrato"
    })
    public static class Assistito {

        @XmlElement(name = "VaccinoSomministrato", required = true)
        protected List<VaccinoSomministrato> vaccinoSomministrato;
        @XmlAttribute(name = "IdAssistito", required = true)
        protected String idAssistito;

        /**
         * Gets the value of the vaccinoSomministrato property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the vaccinoSomministrato property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVaccinoSomministrato().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VaccinoSomministrato }
         * 
         * 
         */
        public List<VaccinoSomministrato> getVaccinoSomministrato() {
            if (vaccinoSomministrato == null) {
                vaccinoSomministrato = new ArrayList<VaccinoSomministrato>();
            }
            return this.vaccinoSomministrato;
        }

        public void setVaccinoSomministrato(List<VaccinoSomministrato> vaccinoSomministrato) {
            this.vaccinoSomministrato = vaccinoSomministrato;
        }

        /**
         * Recupera il valore della proprietà idAssistito.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdAssistito() {
            return idAssistito;
        }

        /**
         * Imposta il valore della proprietà idAssistito.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdAssistito(String value) {
            this.idAssistito = value;
        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="PrincipioVaccinale" maxOccurs="unbounded"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;attribute name="CodAntigene" use="required" type="{}CodAntigene" /&gt;
         *                 &lt;attribute name="Dose" use="required" type="{}Dose" /&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *       &lt;attribute name="TipoTrasmissione" use="required" type="{}TipoTrasmissione" /&gt;
         *       &lt;attribute name="TipoErogatore" use="required" type="{}TipoErogatore" /&gt;
         *       &lt;attribute name="CodiceStruttura" type="{}CodiceStruttura" /&gt;
         *       &lt;attribute name="CodCondizioneSanitaria" use="required" type="{}CodCondizioneSanitaria" /&gt;
         *       &lt;attribute name="CodCategoriaRischio" use="required" type="{}CodCategoriaRischio" /&gt;
         *       &lt;attribute name="CodiceAICVaccino" type="{}CodiceAICVaccino" /&gt;
         *       &lt;attribute name="DenomVaccino" type="{}DenomVaccino" /&gt;
         *       &lt;attribute name="CodTipoFormulazione" use="required" type="{}CodTipoFormulazione" /&gt;
         *       &lt;attribute name="ViaSomministrazione" use="required" type="{}ViaSomministrazione" /&gt;
         *       &lt;attribute name="LottoVaccino" type="{}LottoVaccino" /&gt;
         *       &lt;attribute name="DataScadenza" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
         *       &lt;attribute name="ModalitaPagamento" use="required" type="{}ModalitaPagamento" /&gt;
         *       &lt;attribute name="DataSomministrazione" use="required" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
         *       &lt;attribute name="SitoInoculazione" use="required" type="{}SitoInoculazione" /&gt;
         *       &lt;attribute name="ComuneSomministrazione" type="{}ComuneSomministrazione" /&gt;
         *       &lt;attribute name="AslSomministrazione" type="{}AslSomministrazione" /&gt;
         *       &lt;attribute name="RegioneSomministrazione" type="{}RegioneSomministrazione" /&gt;
         *       &lt;attribute name="StatoEsteroSomministrazione" type="{}StatoEsteroSomministrazione" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "principioVaccinale"
        })
        public static class VaccinoSomministrato {

            @XmlElement(name = "PrincipioVaccinale", required = true)
            protected List<PrincipioVaccinale> principioVaccinale;
            @XmlAttribute(name = "TipoTrasmissione", required = true)
            protected String tipoTrasmissione;
            @XmlAttribute(name = "TipoErogatore", required = true)
            protected String tipoErogatore;
            @XmlAttribute(name = "CodiceStruttura")
            protected String codiceStruttura;
            @XmlAttribute(name = "CodCondizioneSanitaria", required = true)
            protected String codCondizioneSanitaria;
            @XmlAttribute(name = "CodCategoriaRischio", required = true)
            protected String codCategoriaRischio;
            @XmlAttribute(name = "CodiceAICVaccino")
            protected String codiceAICVaccino;
            @XmlAttribute(name = "DenomVaccino")
            protected String denomVaccino;
            @XmlAttribute(name = "CodTipoFormulazione", required = true)
            protected String codTipoFormulazione;
            @XmlAttribute(name = "ViaSomministrazione", required = true)
            protected String viaSomministrazione;
            @XmlAttribute(name = "LottoVaccino")
            protected String lottoVaccino;
            @XmlAttribute(name = "DataScadenza")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar dataScadenza;
            @XmlAttribute(name = "ModalitaPagamento", required = true)
            protected String modalitaPagamento;
            @XmlAttribute(name = "DataSomministrazione", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar dataSomministrazione;
            @XmlAttribute(name = "SitoInoculazione", required = true)
            protected String sitoInoculazione;
            @XmlAttribute(name = "ComuneSomministrazione")
            protected String comuneSomministrazione;
            @XmlAttribute(name = "AslSomministrazione")
            protected String aslSomministrazione;
            @XmlAttribute(name = "RegioneSomministrazione")
            protected String regioneSomministrazione;
            @XmlAttribute(name = "StatoEsteroSomministrazione")
            protected String statoEsteroSomministrazione;

            /**
             * Gets the value of the principioVaccinale property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the Jakarta XML Binding object.
             * This is why there is not a <CODE>set</CODE> method for the principioVaccinale property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPrincipioVaccinale().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link PrincipioVaccinale }
             * 
             * 
             */
            public List<PrincipioVaccinale> getPrincipioVaccinale() {
                if (principioVaccinale == null) {
                    principioVaccinale = new ArrayList<PrincipioVaccinale>();
                }
                return this.principioVaccinale;
            }

            public void setPrincipioVaccinale(List<VaccinazioniSomministrate.Assistito.VaccinoSomministrato.PrincipioVaccinale> principioVaccinale) {
                this.principioVaccinale = principioVaccinale;
            }

            /**
             * Recupera il valore della proprietà tipoTrasmissione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipoTrasmissione() {
                return tipoTrasmissione;
            }

            /**
             * Imposta il valore della proprietà tipoTrasmissione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipoTrasmissione(String value) {
                this.tipoTrasmissione = value;
            }

            /**
             * Recupera il valore della proprietà tipoErogatore.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipoErogatore() {
                return tipoErogatore;
            }

            /**
             * Imposta il valore della proprietà tipoErogatore.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipoErogatore(String value) {
                this.tipoErogatore = value;
            }

            /**
             * Recupera il valore della proprietà codiceStruttura.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodiceStruttura() {
                return codiceStruttura;
            }

            /**
             * Imposta il valore della proprietà codiceStruttura.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodiceStruttura(String value) {
                this.codiceStruttura = value;
            }

            /**
             * Recupera il valore della proprietà codCondizioneSanitaria.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodCondizioneSanitaria() {
                return codCondizioneSanitaria;
            }

            /**
             * Imposta il valore della proprietà codCondizioneSanitaria.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodCondizioneSanitaria(String value) {
                this.codCondizioneSanitaria = value;
            }

            /**
             * Recupera il valore della proprietà codCategoriaRischio.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodCategoriaRischio() {
                return codCategoriaRischio;
            }

            /**
             * Imposta il valore della proprietà codCategoriaRischio.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodCategoriaRischio(String value) {
                this.codCategoriaRischio = value;
            }

            /**
             * Recupera il valore della proprietà codiceAICVaccino.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodiceAICVaccino() {
                return codiceAICVaccino;
            }

            /**
             * Imposta il valore della proprietà codiceAICVaccino.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodiceAICVaccino(String value) {
                this.codiceAICVaccino = value;
            }

            /**
             * Recupera il valore della proprietà denomVaccino.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDenomVaccino() {
                return denomVaccino;
            }

            /**
             * Imposta il valore della proprietà denomVaccino.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDenomVaccino(String value) {
                this.denomVaccino = value;
            }

            /**
             * Recupera il valore della proprietà codTipoFormulazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodTipoFormulazione() {
                return codTipoFormulazione;
            }

            /**
             * Imposta il valore della proprietà codTipoFormulazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodTipoFormulazione(String value) {
                this.codTipoFormulazione = value;
            }

            /**
             * Recupera il valore della proprietà viaSomministrazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getViaSomministrazione() {
                return viaSomministrazione;
            }

            /**
             * Imposta il valore della proprietà viaSomministrazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setViaSomministrazione(String value) {
                this.viaSomministrazione = value;
            }

            /**
             * Recupera il valore della proprietà lottoVaccino.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLottoVaccino() {
                return lottoVaccino;
            }

            /**
             * Imposta il valore della proprietà lottoVaccino.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLottoVaccino(String value) {
                this.lottoVaccino = value;
            }

            /**
             * Recupera il valore della proprietà dataScadenza.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getDataScadenza() {
                return dataScadenza;
            }

            /**
             * Imposta il valore della proprietà dataScadenza.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setDataScadenza(XMLGregorianCalendar value) {
                this.dataScadenza = value;
            }

            /**
             * Recupera il valore della proprietà modalitaPagamento.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getModalitaPagamento() {
                return modalitaPagamento;
            }

            /**
             * Imposta il valore della proprietà modalitaPagamento.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setModalitaPagamento(String value) {
                this.modalitaPagamento = value;
            }

            /**
             * Recupera il valore della proprietà dataSomministrazione.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getDataSomministrazione() {
                return dataSomministrazione;
            }

            /**
             * Imposta il valore della proprietà dataSomministrazione.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setDataSomministrazione(XMLGregorianCalendar value) {
                this.dataSomministrazione = value;
            }

            /**
             * Recupera il valore della proprietà sitoInoculazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSitoInoculazione() {
                return sitoInoculazione;
            }

            /**
             * Imposta il valore della proprietà sitoInoculazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSitoInoculazione(String value) {
                this.sitoInoculazione = value;
            }

            /**
             * Recupera il valore della proprietà comuneSomministrazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getComuneSomministrazione() {
                return comuneSomministrazione;
            }

            /**
             * Imposta il valore della proprietà comuneSomministrazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setComuneSomministrazione(String value) {
                this.comuneSomministrazione = value;
            }

            /**
             * Recupera il valore della proprietà aslSomministrazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAslSomministrazione() {
                return aslSomministrazione;
            }

            /**
             * Imposta il valore della proprietà aslSomministrazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAslSomministrazione(String value) {
                this.aslSomministrazione = value;
            }

            /**
             * Recupera il valore della proprietà regioneSomministrazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRegioneSomministrazione() {
                return regioneSomministrazione;
            }

            /**
             * Imposta il valore della proprietà regioneSomministrazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRegioneSomministrazione(String value) {
                this.regioneSomministrazione = value;
            }

            /**
             * Recupera il valore della proprietà statoEsteroSomministrazione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStatoEsteroSomministrazione() {
                return statoEsteroSomministrazione;
            }

            /**
             * Imposta il valore della proprietà statoEsteroSomministrazione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStatoEsteroSomministrazione(String value) {
                this.statoEsteroSomministrazione = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;attribute name="CodAntigene" use="required" type="{}CodAntigene" /&gt;
             *       &lt;attribute name="Dose" use="required" type="{}Dose" /&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PrincipioVaccinale {

                @XmlAttribute(name = "CodAntigene", required = true)
                protected String codAntigene;
                @XmlAttribute(name = "Dose", required = true)
                protected BigInteger dose;

                /**
                 * Recupera il valore della proprietà codAntigene.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCodAntigene() {
                    return codAntigene;
                }

                /**
                 * Imposta il valore della proprietà codAntigene.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCodAntigene(String value) {
                    this.codAntigene = value;
                }

                /**
                 * Recupera il valore della proprietà dose.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getDose() {
                    return dose;
                }

                /**
                 * Imposta il valore della proprietà dose.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setDose(BigInteger value) {
                    this.dose = value;
                }

            }

        }

    }

}
