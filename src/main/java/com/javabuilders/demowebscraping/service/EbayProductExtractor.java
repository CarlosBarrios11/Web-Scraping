package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Implementación de {@link IProductExtractor} para extraer productos desde eBay utilizando Selenium.
 * <p>
 * Esta clase utiliza Selenium WebDriver para navegar y extraer información de productos en páginas web de eBay.
 * Identifica productos utilizando selectores CSS específicos y extrae atributos como nombre, precio y enlace, asegurándose de que los datos sean válidos antes
 *  * de agregarlos a la lista de productos.
 * <p>
 */
public class EbayProductExtractor implements IProductExtractor{


    private static final Logger log = LoggerFactory.getLogger(EbayProductExtractor.class);

    /**
     * Extrae los productos visibles en la página actual de eBay utilizando WebDriver.
     * Este método espera que los elementos de productos estén presentes en el DOM antes de proceder a extraerlos.
     *
     * @param webDriver El WebDriver que interactúa con la página de eBay.
     * @return Una lista de objetos {@link Product} con los productos extraídos de la página.
     */
    @Override
    public List<Product> scrapeCurrentPage(WebDriver webDriver) {
        List<Product> productList = new ArrayList<>();

        // Espera explícita para asegurarse de que los productos estén cargados en el DOM
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".s-item__info.clearfix")));

        // Selector CSS para localizar los elementos que representan productos
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".s-item__info.clearfix"));

        for (WebElement element : elements) {
            try {
                // Extrae el nombre, precio, enlace.
                String name = element.findElement(By.cssSelector(".s-item__title")).getText().trim();
                String priceText = element.findElement(By.cssSelector(".s-item__price")).getText().trim();
                String link = element.findElement(By.cssSelector(".s-item__link")).getAttribute("href");
                Date timeStamp = new Date();

                if(isProductInformationValid(name, priceText, link)) {
                    productList.add(new Product(name, priceText, link, timeStamp));
                }

        } catch (NoSuchElementException e) {
                // Omitir productos que no se puedan extraer correctamente
                log.error("Error al procesar un producto: {} ", e.getMessage());
            }
        }
        return productList;
    }

    /**
     * Verifica si la información del producto es válida. La validez se determina
     * asegurándose de que el nombre, el precio y el enlace no estén vacíos.
     *
     * @param name El nombre del producto. No puede ser vacío.
     * @param priceText El texto que representa el precio del producto. No puede ser vacío.
     * @param link El enlace del producto. No puede ser vacío.
     * @return {@code true} si el nombre, el precio y el enlace no están vacíos,
     *         {@code false} en caso contrario.
     */
    public boolean isProductInformationValid(String name, String priceText, String link) {
        return !name.isEmpty() && !priceText.isEmpty() && !link.isEmpty();
    }
}
