/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita.parser.regole;

import com.opencsv.bean.CsvBindByPosition;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import lombok.Data;

@Data
public class RecordDtoAvnSomMobilita extends RecordDtoGenerico {

    //MOD_INV~TIP_TRS~COD_REG~ID_ASS~COD_TIP_ERG~COD_STR~COD_CON_SAN~COD_CAT_RIS~COD_AIC~NOM_VAC~COD_TIP_FORM~COD_VIA_SOMM~COD_LOTTO~DAT_SCD~COD_MOD_PAG~DAT_SOMM~COD_SIT_INO~COD_CMN_SOMM~COD_ASL_SOMM~COD_REG_SOMM~COD_STT_SOMM~COD_ANT~DOSE

    @CsvBindByPosition(position = 0)
    private String modalita;

    @CsvBindByPosition(position = 1)
    private String tipoTrasmissione;

    @CsvBindByPosition(position = 2)
    private String codRegione;

    @CsvBindByPosition(position = 3)
    private String idAssistito;

    @CsvBindByPosition(position = 4) //COD_TIP_ERG
    private String tipoErogatore;

    @CsvBindByPosition(position = 5) //COD_STR
    private String codStruttura;

    @CsvBindByPosition(position = 6) //COD_CON_SAN
    private String codCondizioneSanitaria;

    @CsvBindByPosition(position = 7) //COD_CAT_RIS
    private String codCategoriaRischio;

    @CsvBindByPosition(position = 8) //COD_AIC
    private String codiceAICVaccino;

    @CsvBindByPosition(position = 9) //NOM_VAC
    private String denomVaccino;

    @CsvBindByPosition(position = 10) //COD_TIP_FORM
    private String codTipoFormulazione;

    @CsvBindByPosition(position = 11) //COD_VIA_SOMM
    private String viaSomministrazione;

    @CsvBindByPosition(position = 12) //COD_LOTTO
    private String lottoVaccino;

    @CsvBindByPosition(position = 13) //DAT_SCD
    private String dataScadenza;

    @CsvBindByPosition(position = 14) //COD_MOD_PAG
    private String modalitaPagamento;

    @CsvBindByPosition(position = 15) //DAT_SOMM
    private String dataSomministrazione;

    @CsvBindByPosition(position = 16) //COD_SIT_INO
    private String sitoInoculazione;

    @CsvBindByPosition(position = 17) //COD_CMN_SOMM
    private String codiceComuneSomministrazione;

    @CsvBindByPosition(position = 18) //COD_ASL_SOMM
    private String codiceAslSomministrazione;

    @CsvBindByPosition(position = 19) //COD_REG_SOMM
    private String codiceRegioneSomministrazione;

    @CsvBindByPosition(position = 20) //COD_STT_SOMM
    private String statoEsteroSomministrazione;

    @CsvBindByPosition(position = 21) //COD_ANT
    private String codiceAntigene;

    @CsvBindByPosition(position = 22) //DOSE
    private Integer dose;

}
