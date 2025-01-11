package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.Product;
import org.openqa.selenium.WebDriver;
import java.util.List;

/**
 * Interfaz que define el contrato para un extractor de productos en una página web.
 * Las clases que implementen esta interfaz deben proporcionar la lógica para extraer los productos
 * de una página web utilizando un WebDriver.
 */

public interface IProductExtractor {

    /**
     * Extrae una lista de productos de la página web actual utilizando el WebDriver proporcionado.
     * La implementación de este método debe identificar los elementos HTML correspondientes a los productos
     * y extraer su información relevante (como nombre, precio, enlace, etc.).
     *
     * @param webDriver El WebDriver utilizado para interactuar con la página web y extraer los datos.
     * @return Una lista de objetos {@link Product} que representan los productos extraídos de la página.
     */
    List<Product> scrapeCurrentPage(WebDriver webDriver);


}