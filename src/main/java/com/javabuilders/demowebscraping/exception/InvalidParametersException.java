package com.javabuilders.demowebscraping.exception;

/**
 * Excepción personalizada que se lanza cuando los parámetros proporcionados para el proceso de scraping
 * son inválidos o no cumplen con los requisitos esperados.
 * Esta excepción es una subclase de {@link RuntimeException}, lo que la convierte en una excepción no verificada.
 */
public class InvalidParametersException extends RuntimeException{

    /**
     * Constructor de la excepción {@link InvalidParametersException}.
     *
     * @param message El mensaje detallado sobre el error que describe la razón por la cual los parámetros
     *                proporcionados son inválidos.
     */
    public InvalidParametersException(String message) {
        super(message);
    }
}