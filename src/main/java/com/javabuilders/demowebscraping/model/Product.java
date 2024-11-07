package com.javabuilders.demowebscraping.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private String name;
    private double price;
    private String link;

}