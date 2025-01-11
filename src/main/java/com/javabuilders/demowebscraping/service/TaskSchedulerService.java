package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

/**
 * Servicio encargado de programar y gestionar tareas periódicas utilizando un TaskScheduler.
 * Permite programar tareas con un intervalo específico y cancelar tareas previamente programadas.
 */

@Service
public class TaskSchedulerService {

    private final static Logger log = LoggerFactory.getLogger(TaskSchedulerService.class);
    private final TaskScheduler taskScheduler;
    private final ScrapingService scrapingService;
    private ScheduledFuture<?> scheduledTask;



    public TaskSchedulerService(TaskScheduler taskScheduler, ScrapingService scrapingService) {
        this.taskScheduler = taskScheduler;
        this.scrapingService = scrapingService;

    }

    /**
     * Programa una tarea periódica para realizar scraping.
     *
     * @param parameters     Los parámetros de scraping.
     * @param intervalMillis Intervalo en milisegundos entre cada ejecución de la tarea.
     */
    public void scheduleScrapingTask(ScrapingParameters parameters, long intervalMillis) {
        cancelScheduledTask();

        Runnable task = () -> {
            try {
                scrapingService.performScraping(parameters);
            } catch (Exception e) {
                log.error("Error durante la ejecución del scraping: {}", e.getMessage());
            }
        };

        Instant initialDelay = Instant.now().plusMillis(intervalMillis);
        scheduledTask = taskScheduler.scheduleWithFixedDelay(task, initialDelay, Duration.ofMillis(intervalMillis));
        log.info("Nueva tarea programada con un intervalo de {} ms", intervalMillis);
    }


    /**
     * Cancela cualquier tarea de scraping previamente programada.
     * Se asegura de que cualquier tarea pendiente que no haya sido ejecutada o que aún esté en ejecución,
     * sea cancelada antes de iniciar una nueva tarea.
     */
    public void cancelScheduledTask() {

        if(scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
            log.info("Tarea programada cancelada.");
        }

    }
}
