package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.model.Product;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScrapingService {

    private final ProductExtractor productExtractor;

    @Autowired
    public ScrapingService(ProductExtractor productExtractor) {
        this.productExtractor = productExtractor;
    }

    public ScrapingResult result(ScrapingParameters scrapingParameters) {
        WebDriver driver = initializeWebDriver();
        try {
            driver.get(scrapingParameters.getUrl());
            List<Product> products = productExtractor.productList(driver);
            return new ScrapingResult(products);
        } catch (WebDriverException e) {
            System.out.println("Error en la conexión con el navegador: " + e.getMessage());
            return null;
        } finally {
            driver.quit();
        }
    }

    private WebDriver initializeWebDriver() {
        // Configura las opciones de Chrome (puedes configurarlo según tu entorno)
        ChromeOptions options = new ChromeOptions();
        return new ChromeDriver(options);
    }
}

