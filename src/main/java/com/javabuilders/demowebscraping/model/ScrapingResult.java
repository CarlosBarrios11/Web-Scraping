package com.javabuilders.demowebscraping.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Resultado del scraping, que incluye una lista de productos obtenidos.
 */
@Data
@AllArgsConstructor
public class ScrapingResult {
    //Esta lista nos ayudará a tener historial de los productos
    List<Product> products;
}
