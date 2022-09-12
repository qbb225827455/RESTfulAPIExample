package com.example.demo.Model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")

public class Product {

    private String id;
    private String name;
    private int price;
}
