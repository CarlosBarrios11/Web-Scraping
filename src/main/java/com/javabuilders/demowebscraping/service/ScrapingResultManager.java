package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.model.ScrapingResult;
import org.springframework.stereotype.Service;

@Service
public class ScrapingResultManager {

    // Variable para almacenar el último resultado de scraping
    private ScrapingResult latestScrapingResult;

    /**
     * Actualiza los resultados del scraping con los nuevos productos.
     *
     * @param scrapingResult El nuevo resultado del scraping.
     */
    public void updateLatestResult(ScrapingResult scrapingResult) {
        this.latestScrapingResult = scrapingResult;
    }

    /**
     * Obtiene el último resultado de scraping.
     *
     * @return El último resultado del scraping o null si no hay resultados.
     */
    public ScrapingResult getLatestResult() {
        return latestScrapingResult;
    }
}