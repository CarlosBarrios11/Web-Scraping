package com.javabuilders.demowebscraping.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapingParameters {

    private String url; // URL del sitio web a scrapear

    public ScrapingParameters() {
    }
}
