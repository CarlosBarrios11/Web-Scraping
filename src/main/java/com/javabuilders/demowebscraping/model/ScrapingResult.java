package com.javabuilders.demowebscraping.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ScrapingResult {
    List<Product> products;

}
