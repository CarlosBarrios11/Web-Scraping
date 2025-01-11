package com.javabuilders.demowebscraping.service;
import com.javabuilders.demowebscraping.model.ScrapingParameters;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar la configuración y el ciclo de vida de instancias de WebDriver.
 */
@Service
public class WebDriverManager implements IBrowserDriver{

    /**
     * Inicializa y configura una nueva instancia de WebDriver con las opciones especificadas.
     *
     * @return Una instancia configurada de WebDriver.
     */
    public WebDriver initializeWebDriver() {
        // Configura las opciones de Chrome
        ChromeOptions options = new ChromeOptions();

         //2. Deshabilitar las notificaciones emergentes (puede ser útil para scraping)
        //options.addArguments("--disable-notifications");

        // 3. Ignorar los errores de certificado (útil si trabajas con sitios no seguros)
        options.addArguments("--ignore-certificate-errors");

        // 4. Configurar el tamaño de la ventana del navegador (sin que se muestre de forma gráfica)
        options.addArguments("--window-size=1920x1080");

        // 5.  Desactiva el renderizado de imágenes (ideal para scraping puro de datos)
        options.addArguments("--blink-settings=imagesEnabled=false");

        // 6. Desactiva las extensiones del navegador (acelera el tiempo de inicialización)
        options.addArguments("--disable-extensions");

        // 7. Usa menos recursos del sistema (útil en entornos con recursos limitados)
        options.addArguments("--disable-dev-shm-usage");

        // 8. Mejora la seguridad en entornos de servidor
        options.addArguments("--no-sandbox");

         //9. Iniciar en modo incógnito
        //options.addArguments("--incognito");

        // Solo espera a que se cargue el contenido esencial
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        //1. Modo headless: el navegador no se abre visualmente (ideal para pruebas automáticas)
        //options.addArguments("--headless");

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
        WebDriver driver = initializeWebDriver(); // Inicializar el driver
        driver.get(parameters.getUrl()); // Navegar a la URL especificada
        return driver;
    }



    /**
     * Cierra el WebDriver para liberar recursos.
     *
     * @param driver El WebDriver que se está utilizando.
     */
    public static void closeDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit(); // Cerrar el navegador y liberar los recursos
        }
    }
}