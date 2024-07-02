/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.som.mobilita;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@ComponentScan({"it.mds.sdk.flusso.avn.som.mobilita.controller", "it.mds.sdk.flusso.avn.som.mobilita",
		"it.mds.sdk.rest.persistence.entity",
		"it.mds.sdk.libreriaregole.validator",
		"it.mds.sdk.flusso.avn.som.mobilita.service", "it.mds.sdk.flusso.avn.som.mobilita.tracciato",
		"it.mds.sdk.gestoreesiti", "it.mds.sdk.flusso.avn.som.mobilita.parser.regole",
		"it.mds.sdk.flusso.avn.som.mobilita.parser.regole.conf",
		"it.mds.sdk.connettoremds"})
@OpenAPIDefinition(info=@Info(title = "SDK Ministero Della Salute - Flusso VSM", version = "0.0.1-SNAPSHOT", description = "Flusso Vaccinazioni Somministrate Mobilità"))
public class FlussoAvnSomMobilita {

	public static void main(String[] args) {
		SpringApplication.run(FlussoAvnSomMobilita.class, args);
	}

}
