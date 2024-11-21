package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;


/**
 * Servicio encargado de gestionar la programación de tareas de scraping.
 * Utiliza un TaskScheduler para ejecutar el scraping de manera programada, según el intervalo especificado.
 */
@Service
public class IntervalSchedulerService {

    private static final Logger logger= LoggerFactory.getLogger(IntervalSchedulerService.class);

    private final ScrapingService scrapingService;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;


    /**
     * Constructor de la clase. Inicializa el servicio de scraping y el scheduler.
     *
     * @param scrapingService Servicio encargado de realizar el scraping.
     */
    @Autowired
    public IntervalSchedulerService(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) this.taskScheduler).initialize();
    }


    /**
     * Maneja la solicitud de scraping, ejecutando el scraping inmediato o programado según los parámetros.
     * Si el intervalo es "once", se ejecuta de manera inmediata, de lo contrario, se programa el scraping
     * para ejecutarse periódicamente según el intervalo especificado.
     *
     * @param parameters Los parámetros de scraping que definen la URL y el intervalo.
     * @return El resultado del scraping con los productos obtenidos.
     */
    public ScrapingResult handleScrapingRequest(ScrapingParameters parameters) {

        try {
            if ("once".equalsIgnoreCase(parameters.getInterval().trim())) {
                return scrapingService.getFinalList(parameters);
            } else {
                ScrapingResult result = scrapingService.getFinalList(parameters);
                long interval = parseIntervalToMillis(parameters.getInterval());
                scheduledScraping(parameters, interval);
                return result;
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error al procesar el intervalo: {}", e.getMessage());
            return new ScrapingResult(null);
        }
    }

    /**
     * Programa el scraping para ejecutarse periódicamente según el intervalo proporcionado.
     * Cancela cualquier tarea programada previamente y establece una nueva tarea con el intervalo calculado.
     *
     * @param parameters Los parámetros para la ejecución del scraping.
     * @param interval El intervalo en milisegundos entre cada ejecución del scraping.
     */
    private void scheduledScraping(ScrapingParameters parameters, long interval) {
        cancelScheduled();

        // Calcular el instante futuro al cual debe ejecutarse la tarea
        Instant startTime = Instant.now().plusMillis(interval); // Se añade el intervalo al tiempo actual

        /*fixedDelay nos permite esperar a que primero
        termine una ejecución y luego se cuente el tiempo del intervalo */
        scheduledTask = taskScheduler.scheduleWithFixedDelay(
                ()-> scrapingService.getFinalList(parameters),
                startTime,
                Duration.ofMillis(interval));
    }

    /**
     * Convierte el intervalo de tiempo proporcionado (en formato de texto) a milisegundos.
     * Por ejemplo, convierte "1min" a 60000 milisegundos.
     *
     * @param interval El intervalo de tiempo en formato de texto (por ejemplo, "1min", "5min").
     * @return El intervalo en milisegundos.
     * @throws IllegalArgumentException Si el formato del intervalo no es reconocido.
     */
    private long parseIntervalToMillis(String interval) {

        return switch (interval) {
            case "1min" -> 60 * 1000;
            case "5min" -> 5 * 60 * 1000;
            case "10min" -> 10 * 60 * 1000;
            case "30min" -> 30 * 60 * 1000;
            case "1h" -> 60 * 60 * 1000;
            default -> throw new IllegalArgumentException();
        };
    }

    /**
     * Cancela cualquier tarea de scraping previamente programada.
     * Se asegura de que cualquier tarea pendiente que no haya sido ejecutada o que aún esté en ejecución,
     * sea cancelada antes de iniciar una nueva tarea.
     */
    private void cancelScheduled() {

        if(scheduledTask != (null) && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
    }
}