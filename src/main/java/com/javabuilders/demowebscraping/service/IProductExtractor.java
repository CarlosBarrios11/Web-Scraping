package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.Product;
import org.openqa.selenium.WebDriver;
import java.util.List;

/**
 * Interfaz para la extracción de productos desde una página web utilizando Selenium.
 * Cualquier clase que implemente esta interfaz debe proporcionar una implementación
 * para el método `extractProductList` que extrae una lista de productos de una página web.
 */
public interface IProductExtractor {

    /**
     * Método para extraer una lista de productos de una página web utilizando WebDriver de Selenium.
     *
     * @param webDriver El controlador web de Selenium utilizado para navegar en la página.
     * @return Una lista de objetos `Product` con los datos extraídos de la página.
     */
    List<Product> extractProductList(WebDriver webDriver);
}