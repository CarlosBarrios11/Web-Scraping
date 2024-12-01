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
 * Utiliza una búsqueda de elementos en la página con un selector CSS específico para extraer los datos de cada producto.
 * La clase maneja excepciones para continuar con la extracción incluso si algunos productos no pueden ser procesados.
 */

public class EbayProductExtractor implements IProductExtractor{
    private static final Logger logger= LoggerFactory.getLogger(EbayProductExtractor.class);


    /**
     * Extrae una lista de productos desde la página web de eBay.
     * Este método utiliza Selenium WebDriver para localizar los elementos que representan los productos
     * en la página y extrae su nombre, precio y enlace.
     *
     * @param webDriver El controlador web de Selenium, utilizado para interactuar con la página de eBay.
     * @return Una lista de objetos {@link Product} que contienen el nombre, precio, enlace y timestamp del producto.
     */
    @Override
    public List<Product> extractProductList(WebDriver webDriver) {
        List<Product> productList = new ArrayList<>();

        // Espera explícita para asegurarse de que los productos estén cargados
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".s-item__info")));

        // Selector para cada producto en la página
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".s-item__info"));

        for (WebElement element : elements) {
            try {
                // Extrae los detalles del producto (nombre, precio, enlace).
                String name = element.findElement(By.cssSelector(".s-item__title")).getText().trim();
                String priceText = element.findElement(By.cssSelector(".s-item__price")).getText().trim();
                String link = element.findElement(By.cssSelector(".s-item__link")).getAttribute("href");
                //double price = parsingDouble(priceText);
                Date timeStamp = new Date();
                productList.add(new Product(name, priceText, link, timeStamp));

            } catch (NoSuchElementException e) {
                // Omitir productos que no se puedan extraer correctamente
                logger.error("Error al procesar un producto: {} ", e.getMessage());
            }
        }
        return productList;
    }
}
