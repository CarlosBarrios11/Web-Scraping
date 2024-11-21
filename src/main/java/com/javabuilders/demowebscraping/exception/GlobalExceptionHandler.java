package com.javabuilders.demowebscraping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * Clase global encargada de manejar las excepciones a nivel de aplicación.
 * Utiliza {@link ControllerAdvice} para interceptar y gestionar las excepciones lanzadas
 * en cualquier parte de la aplicación, proporcionando respuestas adecuadas en formato JSON.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo {@link InvalidParametersException}, que ocurren cuando los parámetros
     * proporcionados para una solicitud son inválidos.
     *
     * @param ex La excepción {@link InvalidParametersException} que contiene el mensaje de error.
     * @return Una respuesta con un código HTTP 400 (Bad Request) y un mensaje detallado sobre el error.
     */
    @ExceptionHandler(InvalidParametersException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidParameters(InvalidParametersException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja las excepciones de tipo {@link ScrapingExecutionException}, que ocurren cuando hay un error
     * durante la ejecución del proceso de scraping.
     *
     * @param ex La excepción {@link ScrapingExecutionException} que contiene el mensaje de error.
     * @return Una respuesta con un código HTTP 500 (Internal Server Error) y un mensaje detallado sobre el error.
     */
    @ExceptionHandler(ScrapingExecutionException.class)
    public ResponseEntity<Map<String, Object>> handleScrapingExecution(ScrapingExecutionException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Método auxiliar para construir una respuesta de error detallada en formato JSON.
     * Se incluye la marca de tiempo, el código de estado HTTP, el mensaje de error y el texto asociado
     * al estado HTTP.
     *
     * @param message El mensaje de error que se incluirá en la respuesta.
     * @param status El código de estado HTTP a devolver.
     * @return Una respuesta con el código de estado HTTP y el mensaje de error formateado como JSON.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        return ResponseEntity.status(status).body(errorDetails);
    }
}