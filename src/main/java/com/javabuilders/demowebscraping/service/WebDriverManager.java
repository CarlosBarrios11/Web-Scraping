package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

/**
 * Clase encargada de gestionar la inicialización y configuración del WebDriver para el proceso de scraping.
 * Esta clase utiliza Selenium WebDriver para interactuar con el navegador y extraer datos de una página web.
 * Implementa la interfaz {@link IBrowserDriver}.
 */
@Service
public class WebDriverManager implements IBrowserDriver{

    /**
     * Inicializa y configura un WebDriver de Selenium para la extracción de datos utilizando el navegador Google Chrome.
     * La configuración incluye opciones como el modo headless, deshabilitación de notificaciones, y la configuración
     * del tamaño de la ventana.
     *
     * @return Un objeto {@link WebDriver} configurado para navegar en la web.
     */
    @Override
    public WebDriver initializeWebDriver() {
        // Configura las opciones de Chrome (configurarlo según el entorno)
        ChromeOptions options = new ChromeOptions();
        // 1. Modo headless: el navegador no se abre visualmente (ideal para pruebas automáticas)
        options.addArguments("--headless");

        // 2. Deshabilitar las notificaciones emergentes (puede ser útil para scraping)
        options.addArguments("--disable-notifications");

        /* 3. Deshabilitar la GPU (útil para entornos donde no hay hardware gráfico)
        options.addArguments("--disable-gpu"); */

        // 4. Ignorar los errores de certificado (útil si trabajas con sitios no seguros)
        options.addArguments("--ignore-certificate-errors");

        // 5. Configurar el tamaño de la ventana del navegador (sin que se muestre de forma gráfica)
        options.addArguments("--window-size=1920x1080");

        options.addArguments("--incognito");
        return new ChromeDriver(options);
    }

    /**
     * Conecta el WebDriver a la URL especificada en los parámetros de scraping.
     *
     * @param parameters Los parámetros que incluyen la URL del sitio a scrapear.
     * @return Un objeto {@link WebDriver} que ha navegado a la URL proporcionada.
     */
    @Override
    public WebDriver connectDriverToUrl(ScrapingParameters parameters) {
        WebDriver driver = initializeWebDriver();
        driver.get(parameters.getUrl());
        return driver;
    }
}