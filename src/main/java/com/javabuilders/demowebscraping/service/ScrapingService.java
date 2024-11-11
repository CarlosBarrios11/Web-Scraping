package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.model.Product;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de web scraping que realiza la extracción de productos
 * desde una página web utilizando Selenium WebDriver.
 * Esta clase implementa la interfaz {@link IScrapingService}.
 */
@Service
public class ScrapingService implements IScrapingService{

    private final ProductExtractor productExtractor;
    private static final Logger logger= LoggerFactory.getLogger(ScrapingService.class);

    @Getter
    private static List<Product> latestProducts;

    /**
     * Constructor para la inyección de dependencias de {@link ProductExtractor}.
     *
     * @param productExtractor El extractor que se utiliza para obtener los productos de la página web.
     */
    @Autowired
    public ScrapingService(ProductExtractor productExtractor) {
        this.productExtractor = productExtractor;
    }

    /**
     * Realiza el proceso de scraping de productos de acuerdo con los parámetros proporcionados.
     * Se utiliza Selenium WebDriver para navegar a la URL y extraer los productos.
     *
     * @param scrapingParameters Los parámetros necesarios para realizar el scraping (ej. URL de la página).
     * @return Un objeto {@link ScrapingResult} que contiene la lista de productos obtenidos,
     * o null en caso de error en la ejecución.
     */
    public ScrapingResult performScraping(ScrapingParameters scrapingParameters) {
        WebDriver driver = initializeWebDriver();

        try {
            driver.get(scrapingParameters.getUrl());
            List<Product> products = productExtractor.productList(driver);
            latestProducts = products; //Actualizar el resultado en cada ejecución
            logger.info("Productos obtenidos: {}", products.size());  // Log de depuración para ver la cantidad de productos
            return new ScrapingResult(products);
        } catch (WebDriverException e) {
            logger.error("Error en la conexión con el navegador: {}", e.getMessage());
            return null;
        } finally {
            driver.quit();
        }
    }

    /**
     * Inicializa y configura el WebDriver de Selenium para la extracción de datos.
     *
     * @return Un objeto {@link WebDriver} configurado para navegar en la web.
     */
    private WebDriver initializeWebDriver() {
        // Configura las opciones de Chrome (configurarlo según el entorno)
            ChromeOptions options = new ChromeOptions();
            return new ChromeDriver(options);
    }
}


