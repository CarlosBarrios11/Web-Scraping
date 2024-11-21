package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;

/**
 * Interface que define el contrato para el servicio de web scraping.
 * Implementado por la clase {@link ScrapingService}.
 */
public interface IScrapingService {
    /**
     * Realiza el scraping de productos a partir de los parámetros proporcionados.
     *
     * @param scrapingParameters Los parámetros necesarios para realizar el scraping (ej. URL de la página).
     * @return Un objeto {@link ScrapingResult} que contiene la lista de productos obtenidos.
     */
    ScrapingResult getFinalList(ScrapingParameters scrapingParameters);
}