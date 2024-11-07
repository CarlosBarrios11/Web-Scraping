package com.javabuilders.demowebscraping.service;


import com.javabuilders.demowebscraping.model.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductExtractor {
    public List<Product> productList(WebDriver webDriver) {
        List<Product> productList = new ArrayList<>();

        // Espera explícita para asegurarse de que los productos estén cargados
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".s-item__info")));

        // Selector para cada producto en la página
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".s-item__info"));

        for (WebElement element : elements) {

            try {
                String name = element.findElement(By.cssSelector(".s-item__title")).getText().trim();
                String priceText = element.findElement(By.cssSelector(".s-item__price")).getText().trim();
                String link = element.findElement(By.cssSelector(".s-item__link")).getAttribute("href");
                double price = extractPrice(priceText);

                if(price != -1) {
                    productList.add(new Product(name, price, link));
                }
            } catch (Exception e) {
                // Omitir productos que no se puedan extraer correctamente
                System.err.println("Error al procesar un producto: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return productList;
    }

    private double extractPrice(String priceText) {

        if(priceText == null || priceText.isEmpty()) {
            return -1;
        }

        // Elimina los caracteres no numéricos (como $ o ,) del precio
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
