package com.javabuilders.demowebscraping.controller;

import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import com.javabuilders.demowebscraping.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapingController {

    private final ScrapingService scrapingService;


    @Autowired
    public ScrapingController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @PostMapping("/scraping")
    public ScrapingResult runScraping(@RequestBody ScrapingParameters scrapingParameters) {
        return scrapingService.result(scrapingParameters);
    }
}



