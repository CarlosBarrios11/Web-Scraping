package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.model.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Extrae los productos de la página web usando Selenium.
 * Utiliza esperas explícitas para asegurarse de que los productos estén cargados antes de extraerlos.
 */
@Component
public class ProductExtractor {
    private static final Logger logger= LoggerFactory.getLogger(ProductExtractor.class);


    /**
     * Extrae la lista de productos desde la página web.
     * @param webDriver El controlador web de Selenium.
     * @return Una lista de objetos Product con los datos extraídos.
     */
    public List<Product> productList(WebDriver webDriver) {
        List<Product> productList = new ArrayList<>();

        // Espera explícita para asegurarse de que los productos estén cargados
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".s-item__info")));

        // Selector para cada producto en la página
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".s-item__info"));

        for (WebElement element : elements) {
            try {
                // Extrae los detalles del producto (nombre, precio, enlace).
                String name = element.findElement(By.cssSelector(".s-item__title")).getText().trim();
                String priceText = element.findElement(By.cssSelector(".s-item__price")).getText().trim();
                String link = element.findElement(By.cssSelector(".s-item__link")).getAttribute("href");
                double price = extractPrice(priceText);
                Date timeStamp = new Date();

                if(price != -1) {
                    productList.add(new Product(name, price, link, timeStamp));
                }
            } catch (Exception e) {
                // Omitir productos que no se puedan extraer correctamente
                logger.error("Error al procesar un producto: {} ", e.getMessage());
            }
        }
        return productList;
    }

    /**
     * Convierte el texto del precio a un valor numérico.
     * @param priceText El texto del precio.
     * @return El precio como un valor numérico.
     */
    private double extractPrice(String priceText) {

        if(priceText == null || priceText.isEmpty()) {
            return -1;
        }

        // Elimina los caracteres no numéricos (como $ o ",") del precio
        String cleanedPrice = priceText.replaceAll("[^0-9.]", "");

        if(cleanedPrice.indexOf(".") != cleanedPrice.lastIndexOf(".")) {
            return -1;
        }
        try {
            return Double.parseDouble(cleanedPrice);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}
