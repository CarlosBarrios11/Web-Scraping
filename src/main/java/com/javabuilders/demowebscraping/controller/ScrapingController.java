package com.javabuilders.demowebscraping.controller;

import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import com.javabuilders.demowebscraping.service.IntervalSchedulerService;
import com.javabuilders.demowebscraping.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controlador que maneja las solicitudes HTTP relacionadas con el scraping.
 * Incluye un endpoint para ejecutar el scraping y otro para obtener los productos más recientes.
 */
@RestController
public class ScrapingController {

    private final IntervalSchedulerService schedulerService;

    @Autowired
    public ScrapingController(IntervalSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    /**
     * Endpoint que recibe los parámetros de scraping y ejecuta el proceso de scraping.
     *
     * @param scrapingParameters Los parámetros de scraping que definen la URL y el intervalo.
     * @return El resultado del scraping que contiene los productos obtenidos.
     */
    @PostMapping("/scraping")
    public ScrapingResult runScraping(@RequestBody ScrapingParameters scrapingParameters) {
        return schedulerService.handleScrapingRequest(scrapingParameters);
    }

    /**
     * Endpoint que devuelve los productos más recientes obtenidos en el scraping.
     *
     * @return Los productos obtenidos en la última ejecución de scraping.
     */
    @GetMapping("/latest-scraping")
    public ScrapingResult getLatestProducts() {
        return new ScrapingResult(ScrapingService.getLatestProducts());
    }
}



