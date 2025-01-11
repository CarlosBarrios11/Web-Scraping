package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.exception.InvalidParametersException;
import com.javabuilders.demowebscraping.model.Product;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import com.javabuilders.demowebscraping.model.ScrapingResult;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


/**
 * Servicio principal para realizar el scraping en sitios web.
 * Se encarga de gestionar la configuración del WebDriver, la extracción de productos y
 * el manejo de resultados paginados.
 */
@Service
@RequiredArgsConstructor
public class ScrapingService implements IScrapingService {

    private final IBrowserDriver browserDriver;
    private final ScrapingResultManager resultManager;
    private final PaginationHandler paginationHandler;
    private static final Logger logger = LoggerFactory.getLogger(ScrapingService.class);


    /**
     * Realiza el proceso de scraping basado en los parámetros proporcionados.
     *
     * @param parameters Parámetros de scraping, como la URL y número de páginas.
     * @return Un {@link Optional} que contiene el resultado del scraping, o vacío si ocurrió un error.
     */
    @Override
    public Optional<ScrapingResult> performScraping(ScrapingParameters parameters) {

        validateUrl(parameters);

        WebDriver driver = null;
        List<Product> productList = List.of();
        try {
            driver = browserDriver.connectDriverToUrl(parameters);
            IProductExtractor productExtractor = getProductExtractor(parameters);
            productList = scrapeProducts(driver, parameters, productExtractor);

        } catch (Exception e) {
            logger.error("No se pudo realizar el scraping en el método performScraping: {}", e.getMessage(), e);
        } finally {
            logger.info("Productos obtenidos: {}", productList.size());
            WebDriverManager.closeDriver(driver);
        }
        return createScrapingResult(productList);
    }

    /**
     * Obtiene el extractor de productos adecuado basado en los parámetros del scraping.
     *
     * @param parameters Parámetros que definen el tipo de scraping.
     * @return Una implementación de {@link IProductExtractor}.
     */
    private IProductExtractor getProductExtractor(ScrapingParameters parameters) {

        return ExtractorFactory.getProductExtractor(parameters);
    }

    /**
     * Realiza el scraping de los productos utilizando la paginación.
     *
     * @param driver           El WebDriver que interactúa con la página web.
     * @param parameters       Los parámetros de scraping, como el número de páginas.
     * @param productExtractor El extractor de productos.
     * @return Lista de productos extraídos de todas las páginas procesadas.
     */
    private List<Product> scrapeProducts(WebDriver driver, ScrapingParameters parameters, IProductExtractor productExtractor) {

        return paginationHandler.scrapePaginatedResults(driver,
                () -> productExtractor.scrapeCurrentPage(driver),
                parameters,
                productExtractor);
    }

    /**
     * Crea el resultado del scraping y lo almacena utilizando el administrador de resultados.
     *
     * @param products Lista de productos extraídos.
     * @return Un {@link Optional} con el resultado del scraping.
     */
    private Optional <ScrapingResult> createScrapingResult(List<Product> products) {
        ScrapingResult scrapingResult = new ScrapingResult(products);
        resultManager.updateLatestResult(scrapingResult);
        return Optional.of(scrapingResult);
    }

    /**
     * Válida que la URL proporcionada en los parámetros sea válida.
     *
     * @param parameters Parámetros de scraping.
     * @throws InvalidParametersException si la URL está vacía o es nula.
     */
    public void validateUrl(ScrapingParameters parameters) {
        if(parameters.getUrl().isEmpty()) {
            throw new InvalidParametersException("La URL no puede ser nula o vacía.");
        }
    }
}