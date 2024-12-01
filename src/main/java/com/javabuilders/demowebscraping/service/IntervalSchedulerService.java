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
 * Servicio encargado de gestionar solicitudes de scraping, ya sea de manera inmediata o programada.
 * Utiliza servicios de scraping, programación de tareas, y análisis de intervalos para ejecutar
 * y programar tareas de scraping basadas en los parámetros proporcionados.
 */
@Service
public class IntervalSchedulerService {

    private static final Logger logger= LoggerFactory.getLogger(IntervalSchedulerService.class);

    private final ScrapingService scrapingService;
    private final TaskSchedulerService taskScheduler;
    private final IntervalParser intervalParser;

    /**
     * Constructor que inicializa las dependencias necesarias para gestionar el scraping y su programación.
     *
     * @param scrapingService Servicio encargado de realizar el proceso de scraping.
     * @param taskScheduler Servicio encargado de programar y gestionar tareas periódicas.
     * @param intervalParser Servicio encargado de analizar y convertir los intervalos de tiempo a milisegundos.
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
     * Procesa una solicitud de scraping según los parámetros especificados.
     * <p>
     * - Si el intervalo es "once", el scraping se ejecuta inmediatamente.
     * - Si el intervalo es válido, se programa una tarea periódica de scraping.
     * </p>
     *
     * @param parameters Los parámetros que definen la URL de scraping y el intervalo de ejecución.
     *                   Debe incluir un valor válido en el campo "interval".
     * @return Un objeto {@link ScrapingResult} con los productos obtenidos. En caso de error,
     *         se devuelve un resultado vacío.
     * @throws InvalidParametersException Si el intervalo especificado no es válido o no puede analizarse.
     */
    public ScrapingResult handleScrapingRequest(ScrapingParameters parameters) {
        try {
            ScrapingResult result = scrapingService.getFinalList(parameters)
                    .orElse(new ScrapingResult(Collections.emptyList()));

            if ("once".equalsIgnoreCase(parameters.getInterval().trim())) {
               return result;
            } else {
                long interval = intervalParser.parseToMillis(parameters.getInterval());
                taskScheduler.scheduledTask(
                        () -> scrapingService.getFinalList(parameters), interval
                );
            }
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Error al procesar el intervalo: {}", e.getMessage());
            throw new InvalidParametersException("Intervalo inválido recibido em handleScrapingRequest: " + parameters.getInterval());
        }
    }
}