package com.javabuilders.demowebscraping.exception;

/**
 * Excepción personalizada que se lanza cuando ocurre un error durante la ejecución del proceso de scraping.
 * Esta excepción es una subclase de {@link RuntimeException}, lo que la convierte en una excepción no verificada.
 * Se utiliza para indicar fallos en la ejecución del scraping, tales como problemas de conexión, fallos en el análisis de datos,
 * o errores inesperados en el procesamiento.
 */
public class ScrapingExecutionException extends RuntimeException{

    /**
     * Constructor de la excepción {@link ScrapingExecutionException}.
     *
     * @param message El mensaje detallado sobre el error que describe la razón por la cual la ejecución del scraping falló.
     * @param cause La causa subyacente del error, generalmente otra excepción que se lanzó durante el proceso de scraping.
     */
    public ScrapingExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}