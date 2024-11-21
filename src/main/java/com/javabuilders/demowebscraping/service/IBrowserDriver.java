package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import org.openqa.selenium.WebDriver;

/**
 * Interfaz para la configuración y manejo del WebDriver de Selenium.
 * Define los métodos necesarios para inicializar un WebDriver y conectar el driver a una URL.
 * Las implementaciones de esta interfaz se encargarán de configurar y gestionar la interacción con el navegador para el scraping.
 */
public interface IBrowserDriver {

    /**
     * Inicializa el WebDriver necesario para interactuar con el navegador.
     * Este método configura el WebDriver según las necesidades del entorno de ejecución (por ejemplo, Firefox, Chrome).
     *
     * @return Una instancia de {@link WebDriver} lista para usar.
     */
    WebDriver initializeWebDriver();

    /**
     * Conecta el WebDriver a la URL proporcionada en los parámetros de scraping.
     * Utiliza los detalles de la URL especificados en {@link ScrapingParameters} para navegar a la página correcta.
     *
     * @param parameters Los parámetros de scraping que incluyen la URL del sitio web a scrapear.
     * @return Una instancia de {@link WebDriver} que ha navegado a la URL proporcionada.
     */
    WebDriver connectDriverToUrl(ScrapingParameters parameters);
}
