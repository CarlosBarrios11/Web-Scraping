package com.javabuilders.demowebscraping.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


/**
 * Representa un producto obtenido del scraping.
 * Incluye detalles como nombre, precio, enlace y marca de tiempo.
 */
@Data
@AllArgsConstructor
public class Product {

    private String name;
    private double price;
    private String link;
    private Date timeStamp;
}