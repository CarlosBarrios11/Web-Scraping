package com.javabuilders.demowebscraping.model;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Parámetros necesarios para realizar un scraping.
 * Incluye la URL y el intervalo entre ejecuciones del scraping.
 */
@Data
@AllArgsConstructor
public class ScrapingParameters {

    private String url; // URL del sitio web a scrapear
    private String interval;


    public ScrapingParameters() {
    }
}
