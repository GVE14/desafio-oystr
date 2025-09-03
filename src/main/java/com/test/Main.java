package com.test;

import com.test.domain.MachineEntity;
import com.test.interfaces.Bot;

import com.test.interfaces.Machine;
import com.test.service.MachineExcelExporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/*
 * The application MUST be developed in Java using the concepts of Object Oriented Programming.
 * You may also use concepts such as Dependency Injection and Inversion of Control.
 * You may modify the main method to better suit your approach, but you MAY NOT change the concept itself.
 */
@SpringBootApplication
public class Main {


	public static void main(String[] args) {

		final Logger logger = LoggerFactory.getLogger(Main.class);

		ApplicationContext context = SpringApplication.run(Main.class, args);

		Bot bot = context.getBean(Bot.class);
		MachineExcelExporterService excelService = context.getBean(MachineExcelExporterService.class);

		List<MachineEntity> list = new ArrayList<>();

		String[] urls = new String[]{
				"https://www.agrofy.com.br/trator-john-deere-6190j-205317.html",
				"https://www.agrofy.com.br/trator-john-deere-7225j-208433.html",
				"https://www.tratoresecolheitadeiras.com.br/veiculo/uberlandia/mg/plataforma-colheitadeira/gts/flexer-xs-45/2023/45-pes/draper/triamaq-tratores/1028839",
				"https://www.tratoresecolheitadeiras.com.br/veiculo/campo-mourao/pr/trator/case/case-farmall-120/2017/tracao-4x4/cabine-cabinado/zanella-tratores/1307640",
				"https://www.mercadomaquinas.com.br/anuncio/247237-retro-escavadeira-caterpillar-416f2-2019-curitiba-pr",
				"https://www.mercadomaquinas.com.br/anuncio/236623-mini-escavadeira-bobcat-e27z-2019-sete-lagoas-mg"
		};

		for (String url : urls) {
			try {
				Machine machinery = bot.fetch(url);
				list.add((MachineEntity) machinery);
			} catch (Exception e) {
				logger.error("Error processing URL: {} - {}", url, e.getMessage());
			}
		}

		try {
			excelService.export(list, "spreadsheet/machines.xlsx");
			logger.info("Spreadsheet generated successfully!");
		} catch (Exception e) {
			logger.error("Error generating spreadsheet: {}", e.getMessage());
		}
	}

}