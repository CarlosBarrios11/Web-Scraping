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
import java.util.Optional;


/**
 * Servicio encargado de realizar operaciones de web scraping.
 * <p>
 * Utiliza Selenium WebDriver para conectarse a páginas web y extraer información específica
 * (en este caso, productos) basada en los parámetros proporcionados.
 * </p>
 * <p>
 * También gestiona los resultados obtenidos mediante el uso de {@link ScrapingResultManager}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ScrapingService implements IScrapingService {

    private final IBrowserDriver browserDriver;
    private final ScrapingResultManager resultManager;
    private static final Logger logger = LoggerFactory.getLogger(ScrapingService.class);

    /**
     * Realiza el scraping de productos desde una página web especificada en los parámetros.
     * <p>
     * Este método conecta a la URL proporcionada usando Selenium WebDriver, extrae los productos
     * y gestiona los resultados para que puedan ser utilizados por otras partes del sistema.
     * </p>
     *
     * @param scrapingParameters Los parámetros necesarios para realizar el scraping, incluyendo la URL.
     * @return Un {@link Optional} que contiene un objeto {@link ScrapingResult} con los productos extraídos,
     * o un valor vacío si ocurre un error en la ejecución.
     * @throws InvalidParametersException Si los parámetros proporcionados no son válidos.
     * @throws ScrapingExecutionException Si ocurre un error durante el proceso de scraping.
     */
    public Optional<ScrapingResult> getFinalList(ScrapingParameters scrapingParameters) {


        validateUrl(scrapingParameters);
        WebDriver driver = browserDriver.connectDriverToUrl(scrapingParameters);

        try {
            IProductExtractor productExtractor = ExtractorFactory.getProductExtractor(scrapingParameters);
            List<Product> products = productExtractor.extractProductList(driver);
            logger.info("Productos obtenidos: {}", products.size());  // Log de depuración para ver la cantidad de productos
            ScrapingResult latestProducts = new ScrapingResult(products);
            resultManager.updateLatestResult(latestProducts);
            return Optional.of(latestProducts);
        } catch (WebDriverException e) {
            logger.error("Error en la conexión con el navegador: {}", e.getMessage());
            throw new ScrapingExecutionException("Error en la ejecución del scraping.", e);
        } finally {
            driver.quit();
        }
    }

    /**
     * Válida los parámetros de scraping para asegurarse de que contienen una URL válida.
     * <p>
     * Este método lanza una excepción si la URL está vacía o es nula, asegurando que el proceso
     * de scraping no se inicie con datos inválidos.
     * </p>
     *
     * @param parameters Los parámetros que contienen la URL para el scraping.
     * @throws InvalidParametersException Si la URL está vacía o es nula.
     */
    public void validateUrl(ScrapingParameters parameters) {
        if(parameters.getUrl().isEmpty()) {
            throw new InvalidParametersException("La URL no puede ser nula o vacía.");
        }
    }
}