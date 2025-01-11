package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;

import java.util.Optional;

/**
 * Interfaz que define el contrato para un servicio de scraping de datos.
 * Las clases que implementen esta interfaz deben proporcionar la lógica para realizar
 * el scraping de una página web utilizando los parámetros de entrada proporcionados.
 */
public interface IScrapingService {
    /**
     * Realiza el scraping de datos de una página web utilizando los parámetros proporcionados.
     * El método debe extraer la información solicitada según los parámetros y devolver un objeto
     * {@link ScrapingResult} que contiene los datos extraídos, si el scraping fue exitoso.
     *
     * @param scrapingParameter Los parámetros necesarios para configurar y ejecutar el scraping, como la URL y otras configuraciones.
     * @return Un {@link Optional} que contiene un {@link ScrapingResult} con los datos extraídos si la operación es exitosa,
     *         o un {@link Optional#empty()} si ocurre algún error o no se encuentran datos.
     */
    Optional<ScrapingResult> performScraping(ScrapingParameters scrapingParameter);


}