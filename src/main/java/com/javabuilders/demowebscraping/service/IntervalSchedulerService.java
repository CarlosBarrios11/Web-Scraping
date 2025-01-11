package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.exception.InvalidParametersException;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * Servicio para manejar las solicitudes de raspado y programar la ejecución de tareas de scraping.
 * Este servicio coordina la ejecución del scraping, y en caso de que se solicite una ejecución repetida,
 * programa la tarea para ejecutarse en intervalos regulares.
 */
@Service
public class IntervalSchedulerService {

    private static final Logger logger= LoggerFactory.getLogger(IntervalSchedulerService.class);

    private final ScrapingService scrapingService;
    private final TaskSchedulerService taskScheduler;
    private final IntervalParser intervalParser;

    /**
     * Constructor que inyecta las dependencias necesarias para el funcionamiento del servicio.
     *
     * @param scrapingService Servicio que realiza el scraping de datos.
     * @param taskScheduler Servicio que gestiona la programación de tareas.
     * @param intervalParser Analiza y convierte intervalos de tiempo a milisegundos.
     */
    @Autowired
    public IntervalSchedulerService(ScrapingService scrapingService,
                                    TaskSchedulerService taskScheduler,
                                    IntervalParser intervalParser) {
        this.scrapingService = scrapingService;
        this.taskScheduler = taskScheduler;
        this.intervalParser = intervalParser;

    }


    /**
     * Maneja la solicitud del scraping, ejecutándolo una vez o programando la tarea para repetirse
     * en intervalos regulares según el parámetro de intervalo proporcionado.
     *
     * @param parameters Parámetros que contienen la URL y el intervalo para el scraping.
     * @return El resultado del scraping, que puede ser una lista vacía si no se encontraron productos.
     * @throws InvalidParametersException Si el intervalo proporcionado es inválido.
     */

    public ScrapingResult handleScrapingRequest(ScrapingParameters parameters) {

        try {

            ScrapingResult result = scrapingService.performScraping(parameters)
                    .orElse(new ScrapingResult(Collections.emptyList()));


            if ("once".equalsIgnoreCase(parameters.getInterval().trim())) {
                return result;
            } else {
                long interval = intervalParser.parseToMillis(parameters.getInterval());
                taskScheduler.scheduleScrapingTask(parameters, interval);

            }
            return result;


        } catch (IllegalArgumentException e) {
            logger.error("Error al procesar el intervalo: {}", e.getMessage());
            throw new InvalidParametersException("Intervalo inválido recibido em handleScrapingRequest: " + parameters.getInterval());
        }
    }
}