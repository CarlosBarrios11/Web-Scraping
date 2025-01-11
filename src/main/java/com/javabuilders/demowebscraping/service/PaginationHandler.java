package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.Product;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Servicio encargado de manejar la navegación y el scraping de resultados paginados
 * en un sitio web mediante Selenium WebDriver.
 */
@Service
public class PaginationHandler {

    private static final Logger log = LoggerFactory.getLogger(PaginationHandler.class);

/**
 * Realiza el scraping de múltiples páginas según los parámetros especificados.
 * Este método navega a través de múltiples páginas, extrae los datos en cada página
 * y acumula los resultados en una lista de productos.
 *
 * @param driver El WebDriver que interactúa con el sitio web.
 * @param scraperFunction Una función que realiza el scraping de la página actual y retorna una lista de productos.
 * @param parameters Los parámetros de scraping, incluyendo el número de páginas a procesar.
 * @param productExtractor El extractor de productos que define cómo se extraen los productos de cada página.
 * @return Una lista de productos extraídos de todas las páginas procesadas.
 */

    public List <Product> scrapePaginatedResults (WebDriver driver, Supplier<List<Product>> scraperFunction,
                                                 ScrapingParameters parameters, IProductExtractor productExtractor) {
        List<Product> allResults = new ArrayList<>();

        int pagesToScrape = parameters.getPages();

        for (int i = 0; i < pagesToScrape; i++) {

            try {
                log.info("Scrapeando página {} de {}", i + 1, pagesToScrape);

                allResults.addAll(scraperFunction.get());


                if (i < pagesToScrape - 1) {
                    log.info("Navegando a la siguiente página...");
                    if (!clickNextPageButton(driver)) {
                        log.info("Paginación finalizada. Todas las páginas disponibles han sido procesadas.");
                        break;
                    }
                } else {
                    log.info("Se completó el scraping de la última página.");
                }
            } catch (TimeoutException e) {
                log.error("No se encontró el botón 'Siguiente', probablemente no haya más páginas. {}", e.getMessage(), e);
                break; // Detener si no hay más páginas
            }
        }
        return allResults;
    }


    /**
     * Navega a la siguiente página en el sitio web haciendo clic en el botón "Siguiente".
     *
     * @param driver El WebDriver utilizado para interactuar con la página web.
     * @return {@code true} si la navegación a la siguiente página fue exitosa, {@code false} si no fue posible avanzar.
     */
    private boolean clickNextPageButton(WebDriver driver) {
        try {
            WebElement nextButton = new WebDriverWait(driver, Duration.ofMillis(1000))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-label='Ir a la página de búsqueda siguiente']")));

            if (nextButton.isDisplayed() && nextButton.isEnabled()) {
                nextButton.click();
                return true;
            } else {
                log.warn("Se terminó el scraping debido a que ya no hay más páginas a scrapear");
            }
        } catch (Exception e) {
            log.warn("Falló presionar botón de siguiente página: {}", e.getMessage(), e);
        }
        return false;
    }
}
