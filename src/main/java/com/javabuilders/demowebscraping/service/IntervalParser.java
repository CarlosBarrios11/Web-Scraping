package com.javabuilders.demowebscraping.service;

import org.springframework.stereotype.Component;

/**
 * Componente responsable de convertir intervalos de tiempo en texto a su representación en milisegundos.
 * <p>
 * Este componente es útil para traducir formatos legibles por humanos (como "1min" o "1h")
 * a un formato numérico utilizado en tareas programadas.
 * </p>
 */

@Component
public class IntervalParser {

    /**
     * Convierte un intervalo de tiempo en formato de texto a milisegundos.
     * <p>
     * Este metodo interpreta los siguientes valores:
     * <ul>
     *     <li>"1min" -> 60,000 ms (1 minuto)</li>
     *     <li>"5min" -> 300,000 ms (5 minutos)</li>
     *     <li>"10min" -> 600,000 ms (10 minutos)</li>
     *     <li>"30min" -> 1,800,000 ms (30 minutos)</li>
     *     <li>"1h" -> 3,600,000 ms (1 hora)</li>
     * </ul>
     * Cualquier valor no reconocido generará una excepción.
     * </p>
     *
     * @param interval El intervalo de tiempo en formato de texto (por ejemplo, "1min", "1h").
     * @return El intervalo convertido a milisegundos.
     * @throws IllegalArgumentException Si el intervalo no es válido o no está soportado.
     */
    public long parseToMillis(String interval) {

        return switch (interval.trim().toLowerCase()) {
            case "1min" -> 60 * 1000;
            case "5min" -> 5 * 60 * 1000;
            case "10min" -> 10 * 60 * 1000;
            case "30min" -> 30 * 60 * 1000;
            case "1h" -> 60 * 60 * 1000;
            default -> throw new IllegalArgumentException("Intervalo desconocido: " + interval);
        };
    }
}
