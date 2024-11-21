package com.javabuilders.demowebscraping.controller;

import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import com.javabuilders.demowebscraping.service.IntervalSchedulerService;
import com.javabuilders.demowebscraping.service.ScrapingResultManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controlador que maneja las solicitudes HTTP relacionadas con el proceso de scraping de productos.
 * Proporciona dos endpoints:
 * <ul>
 *   <li>Un endpoint POST para ejecutar el proceso de scraping de acuerdo con los parámetros proporcionados.</li>
 *   <li>Un endpoint GET para obtener los productos más recientes obtenidos del scraping.</li>
 * </ul>
 */
@RestController
public class ScrapingController {

    private final IntervalSchedulerService schedulerService;
    private final ScrapingResultManager resultManager;

    /**
     * Constructor del controlador. Se inyectan los servicios necesarios para ejecutar el scraping
     * y gestionar los resultados.
     *
     * @param schedulerService El servicio encargado de gestionar las solicitudes de scraping y su programación.
     * @param resultManager El servicio encargado de manejar los resultados obtenidos del scraping.
     */
    @Autowired
    public ScrapingController(IntervalSchedulerService schedulerService, ScrapingResultManager resultManager) {
        this.schedulerService = schedulerService;
        this.resultManager = resultManager;
    }

    /**
     * Endpoint que recibe los parámetros necesarios para ejecutar el proceso de scraping.
     * Inicia el scraping y devuelve los resultados obtenidos.
     *
     * @param scrapingParameters Los parámetros necesarios para configurar y ejecutar el scraping,
     *                           como la URL y el intervalo de tiempo entre cada ejecución.
     * @return El resultado del scraping, que incluye la lista de productos obtenidos.
     */
    @PostMapping("/scraping")
    public ScrapingResult runScraping(@RequestBody ScrapingParameters scrapingParameters) {
        return schedulerService.handleScrapingRequest(scrapingParameters);
    }

    /**
     * Endpoint que devuelve los productos más recientes obtenidos en la última ejecución de scraping.
     * Si no ha habido ejecuciones recientes, podría devolver un resultado vacío.
     *
     * @return Un objeto {@link ScrapingResult} con los productos obtenidos en la última ejecución de scraping.
     */
    @GetMapping("/latest-scraping")
    public ScrapingResult getLatestProducts() {
        return resultManager.getLatestResult();
    }
}