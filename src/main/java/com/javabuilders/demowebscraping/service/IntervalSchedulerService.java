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
 * Servicio encargado de gestionar el intervalo y la programación de tareas de scraping.
 * Utiliza un TaskScheduler para ejecutar scraping de manera programada según el intervalo.
 */
@Service
public class IntervalSchedulerService {

    private static final Logger logger= LoggerFactory.getLogger(IntervalSchedulerService.class);

    private final ScrapingService scrapingService;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    @Autowired
    public IntervalSchedulerService(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) this.taskScheduler).initialize();
    }


    /**
     * Maneja la solicitud de scraping, ejecutando el scraping inmediato o programado según los parámetros.
     *
     * @param parameters Los parámetros de scraping que definen la URL y el intervalo.
     * @return El resultado del scraping.
     */
    public ScrapingResult handleScrapingRequest(ScrapingParameters parameters) {

        try {
            if ("once".equalsIgnoreCase(parameters.getInterval().trim())) {
                return scrapingService.performScraping(parameters);
            } else {
                ScrapingResult result = scrapingService.performScraping(parameters);
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
     * Programa el scraping para ejecutarse periódicamente según el intervalo.
     * @param parameters Parámetros para el scraping.
     * @param interval El intervalo en milisegundos.
     */
    private void scheduledScraping(ScrapingParameters parameters, long interval) {
        cancelScheduled();

        // Calcular el instante futuro al cual debe ejecutarse la tarea
        Instant startTime = Instant.now().plusMillis(interval); // Se añade el intervalo al tiempo actual

        /*fixedDelay nos permite esperar a que primero
        termine una ejecución y luego se cuente el tiempo del intervalo */
        scheduledTask = taskScheduler.scheduleWithFixedDelay(
                ()-> scrapingService.performScraping(parameters),
                startTime,
                Duration.ofMillis(interval));
    }


    /**
     * Convierte el intervalo de tiempo proporcionado en formato de texto a milisegundos.
     *
     * @param interval El intervalo en formato de texto (por ejemplo, "1min", "5min").
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
     */
    private void cancelScheduled() {

        if(scheduledTask != (null) && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
    }
}



