package com.javabuilders.demowebscraping.service;

import com.javabuilders.demowebscraping.model.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Implementación de {@link IProductExtractor} para extraer productos desde eBay utilizando Selenium.
 * Utiliza una búsqueda de elementos en la página con un selector CSS específico para extraer los datos de cada producto.
 * La clase maneja excepciones para continuar con la extracción incluso si algunos productos no pueden ser procesados.
 */
@Service
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
        //WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        //wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".s-item__info")));

        // Selector para cada producto en la página
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".s-item__info"));

        for (WebElement element : elements) {
            try {
                // Extrae los detalles del producto (nombre, precio, enlace).
                String name = element.findElement(By.cssSelector(".s-item__title")).getText().trim();
                String priceText = element.findElement(By.cssSelector(".s-item__price")).getText().trim();
                String link = element.findElement(By.cssSelector(".s-item__link")).getAttribute("href");
                double price = parsingDouble(priceText);
                Date timeStamp = new Date();

                if(price != -1) {
                    productList.add(new Product(name, price, link, timeStamp));
                }
            } catch (NoSuchElementException e) {
                // Omitir productos que no se puedan extraer correctamente
                logger.error("Error al procesar un producto: {} ", e.getMessage());
            }
        }
        return productList;
    }


    /**
     * Convierte el texto del precio a un valor numérico.
     * Elimina caracteres no numéricos como símbolos de moneda y comas, y maneja posibles errores de formato.
     *
     * @param priceText El texto del precio (puede incluir símbolos de moneda, comas, etc.).
     * @return El precio como un valor numérico {@link double}. Si no se puede parsear, retorna -1.
     */
    private double parsingDouble(String priceText) {

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
