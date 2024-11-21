package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.exception.InvalidParametersException;
import com.javabuilders.demowebscraping.exception.ScrapingExecutionException;
import com.javabuilders.demowebscraping.model.Product;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementación del servicio de web scraping que realiza la extracción de productos
 * desde una página web utilizando Selenium WebDriver.
 * Esta clase implementa la interfaz {@link IScrapingService}.
 */
@Service
@RequiredArgsConstructor
public class ScrapingService implements IScrapingService {

    private final IBrowserDriver browserDriver;
    private final ScrapingResultManager resultManager;
    private static final Logger logger = LoggerFactory.getLogger(ScrapingService.class);

    /**
     * Realiza el proceso de scraping de productos de acuerdo con los parámetros proporcionados.
     * Se utiliza Selenium WebDriver para navegar a la URL y extraer los productos.
     *
     * @param scrapingParameters Los parámetros necesarios para realizar el scraping (ej. URL de la página).
     * @return Un objeto {@link ScrapingResult} que contiene la lista de productos obtenidos,
     * o null en caso de error en la ejecución.
     */
    public ScrapingResult getFinalList(ScrapingParameters scrapingParameters) {


        validateUrl(scrapingParameters);
        WebDriver driver = browserDriver.connectDriverToUrl(scrapingParameters);

        try {
            IProductExtractor productExtractor = ExtractorFactory.getProductExtractor(scrapingParameters);
            List<Product> products = productExtractor.extractProductList(driver);
            logger.info("Productos obtenidos: {}", products.size());  // Log de depuración para ver la cantidad de productos
            ScrapingResult latestProducts = new ScrapingResult(products);
            resultManager.updateLatestResult(latestProducts);
            return latestProducts;
        } catch (WebDriverException e) {
            logger.error("Error en la conexión con el navegador: {}", e.getMessage());
            throw new ScrapingExecutionException("Error en la ejecución del scraping.", e);
        } finally {
            driver.quit();
        }
    }

    /**
     * Valida que la URL no esté vacía.
     *
     * @param parameters Los parámetros de scraping que contienen la URL.
     */
    public void validateUrl(ScrapingParameters parameters) {
        if(parameters.getUrl().isEmpty()) {
            throw new InvalidParametersException("La URL no puede ser nula o vacía.");
        }
    }
}