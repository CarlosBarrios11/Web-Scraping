package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.model.ScrapingParameters;

/**
 *Fábrica para obtener el extractor de productos adecuado según la URL del sitio web de destino.
 */
public class ExtractorFactory {


    private static final String ebayWebSite = "www.ebay.com";


    /**
     * Devuelve una instancia de un extractor de productos basado en la URL proporcionada.
     * Actualmente, solo soporta eBay como sitio web de destino. Si la URL contiene el dominio de eBay,
     * se devuelve un {@link EbayProductExtractor}. Si no se reconoce la URL, se lanza una excepción.
     *
     * @param scrapingParameters Parámetros de raspado que contienen la URL del sitio web a scrapear.
     * @return Una instancia de {@link IProductExtractor} para extraer productos del sitio web especificado.
     * @throws IllegalArgumentException Si el sitio web no es compatible (actualmente solo soporta eBay).
     */

    public static IProductExtractor getProductExtractor(ScrapingParameters scrapingParameters) {
        String url = scrapingParameters.getUrl().toLowerCase();

        if(url.contains(ebayWebSite)) {
            return new EbayProductExtractor();
        }
        throw new IllegalArgumentException("No extractor available for this site.");
    }
}