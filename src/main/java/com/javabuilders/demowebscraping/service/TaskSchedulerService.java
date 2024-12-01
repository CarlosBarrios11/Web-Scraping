package com.javabuilders.demowebscraping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
    private ScheduledFuture<?> scheduledTask;

    public TaskSchedulerService() {
        this.taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler)this.taskScheduler).initialize();
    }

    /**
     * Programa una tarea para ejecutarse periódicamente con un intervalo específico.
     * Si existe una tarea previamente programada, esta se cancela antes de programar la nueva.
     *
     * @param task          La tarea a ejecutar periódicamente.
     * @param intervalMillis El intervalo de tiempo entre ejecuciones, en milisegundos.
     */
    public void scheduledTask(Runnable task, long intervalMillis) {
        cancelScheduledTask();
        Instant starTime = Instant.now().plusMillis(intervalMillis);
        scheduledTask = taskScheduler.scheduleWithFixedDelay(task, starTime, Duration.ofMillis(intervalMillis));
        log.info("Nueva tarea programada con un intervalo de {} ms", intervalMillis);
    }


    /**
     * Cancela cualquier tarea de scraping previamente programada.
     * Se asegura de que cualquier tarea pendiente que no haya sido ejecutada o que aún esté en ejecución,
     * sea cancelada antes de iniciar una nueva tarea.
     */
    private void cancelScheduledTask() {

        if(scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
            log.info("Tarea programada cancelada.");
        }

    }
}
