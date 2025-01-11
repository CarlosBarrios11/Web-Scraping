package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import org.openqa.selenium.WebDriver;

/**
 * Interfaz para interactuar con un navegador web en un entorno de scraping de datos.
 * Esta interfaz define métodos para inicializar el WebDriver y conectar el navegador a una URL específica.
 */
public interface IBrowserDriver {

    /**
     * Inicializa el WebDriver necesario para controlar el navegador.
     * Este método debe ser implementado para proporcionar la configuración específica del WebDriver
     * (por ejemplo, el tipo de navegador a utilizar: Chrome, Firefox, etc.).
     *
     * @return Una instancia de {@link WebDriver} lista para usar.
     */
    WebDriver initializeWebDriver();

    /**
     * Conecta el WebDriver a una URL específica utilizando los parámetros del scraper proporcionados.
     * Este método permite navegar hacia la URL que se desea raspar, usando la configuración de parámetros adecuada.
     *
     * @param parameters Parámetros del scraper que contienen la URL y otras configuraciones necesarias.
     * @return Una instancia de {@link WebDriver} conectada a la URL especificada.
     */
    WebDriver connectDriverToUrl(ScrapingParameters parameters);

}


