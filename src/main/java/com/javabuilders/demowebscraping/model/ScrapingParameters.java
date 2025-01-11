package com.javabuilders.demowebscraping.model;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Parámetros necesarios para realizar un scraping.
 * Incluye la URL, el intervalo entre ejecuciones del scraping y el número de páginas a extraer información.
 */
@Data
@AllArgsConstructor
public class ScrapingParameters {
    private String url;
    private String interval;
    private final int pages;
}
