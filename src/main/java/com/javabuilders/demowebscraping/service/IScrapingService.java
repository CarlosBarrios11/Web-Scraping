package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;

import java.util.Optional;

/**
 * Interfaz que define el contrato para el servicio de web scraping.
 * <p>
 * Esta interfaz permite implementar servicios que realicen la extracción de datos desde páginas web,
 * garantizando que cualquier clase que implemente este contrato ofrezca la funcionalidad necesaria
 * para ejecutar scraping basándose en parámetros específicos.
 * </p>
 * <p>
 * La implementación principal de esta interfaz es la clase {@link ScrapingService}.
 * </p>
 */
public interface IScrapingService {
    /**
     * Realiza el proceso de scraping de productos utilizando los parámetros proporcionados.
     * <p>
     * Este método se encarga de validar los parámetros y ejecutar la lógica de scraping,
     * devolviendo los resultados obtenidos o un estado vacío en caso de que no haya datos.
     * </p>
     *
     * @param scrapingParameters Los parámetros necesarios para realizar el scraping,
     *                           como la URL de la página y cualquier configuración adicional.
     * @return Un objeto {@link Optional} que contiene un {@link ScrapingResult} con la lista de productos
     * obtenidos, o un estado vacío si no se encontraron productos o hubo un error en el proceso.
     */
    Optional<ScrapingResult> getFinalList(ScrapingParameters scrapingParameters);
}