package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.model.ScrapingParameters;

/**
 * Fábrica encargada de proporcionar la implementación de {@link IProductExtractor} adecuada según la URL proporcionada.
 * Actualmente, soporta solo la extracción de productos de eBay, pero puede ampliarse para soportar otros sitios en el futuro.
 */
public class ExtractorFactory {

    private static final String ebayWebSite = "www.ebay.com";

    /**
     * Obtiene una instancia de {@link IProductExtractor} según los parámetros de scraping proporcionados.
     * <p>
     * El método verifica la URL proporcionada en los parámetros de scraping y decide qué extractor usar.
     * Actualmente, solo se soporta el extractor de eBay, pero la clase puede ser extendida para incluir más sitios web.
     *
     * @param scrapingParameters Los parámetros de scraping, que contienen la URL de la página web a extraer productos.
     * @return Una instancia de {@link IProductExtractor} correspondiente a la URL proporcionada.
     * @throws IllegalArgumentException Si no hay un extractor disponible para la URL proporcionada.
     */

    public static IProductExtractor getProductExtractor(ScrapingParameters scrapingParameters) {
        String url = scrapingParameters.getUrl().toLowerCase();

        if(url.contains(ebayWebSite)) {
            return new EbayProductExtractor();
        }
        throw new IllegalArgumentException("No extractor available for this site.");
    }
}