package com.example.demo.Model.Product;

import lombok.Data;

@Data
public class ProductResponse {

    private String id;
    private String name;
    private int price;
    private String creator;
}