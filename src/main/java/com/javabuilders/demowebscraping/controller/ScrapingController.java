package com.javabuilders.demowebscraping.controller;

import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import com.javabuilders.demowebscraping.service.IntervalSchedulerService;
import com.javabuilders.demowebscraping.service.ScrapingResultManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controlador encargado de manejar las solicitudes HTTP relacionadas con el proceso de scraping de productos.
 * <p>
 * Este controlador proporciona dos endpoints para interactuar con el sistema de scraping:
 * </p>
 * <ul>
 *   <li>Un endpoint POST para ejecutar el proceso de scraping de acuerdo con los parámetros proporcionados.</li>
 *   <li>Un endpoint GET para obtener los productos más recientes obtenidos del scraping.</li>
 * </ul>
 * <p>
 * Utiliza los servicios {@link IntervalSchedulerService} para manejar la programación de scraping y {@link ScrapingResultManager}
 * para gestionar los resultados de las ejecuciones.
 * </p>
 */
@RestController
public class ScrapingController {

    private final IntervalSchedulerService schedulerService;
    private final ScrapingResultManager resultManager;

    /**
     * Constructor del controlador que inyecta los servicios necesarios para ejecutar el scraping y gestionar los resultados.
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
     * Endpoint para ejecutar el proceso de scraping a partir de los parámetros proporcionados en la solicitud.
     * Este endpoint inicia el scraping y devuelve los resultados obtenidos.
     * <p>
     * Si el resultado del scraping está vacío, devuelve un estado HTTP 204 No Content.
     * Si se obtienen productos, devuelve un estado HTTP 200 OK con la lista de productos obtenidos.
     * </p>
     *
     * @param scrapingParameters Los parámetros necesarios para configurar y ejecutar el scraping,
     *                           como la URL y el intervalo de tiempo entre cada ejecución.
     * @return Una respuesta con el resultado del scraping, que incluye la lista de productos obtenidos.
     */
    @PostMapping("/scraping")
    public ResponseEntity<ScrapingResult> runScraping(@RequestBody ScrapingParameters scrapingParameters) {
        ScrapingResult result = schedulerService.handleScrapingRequest(scrapingParameters);

        if(result.getProducts().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
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