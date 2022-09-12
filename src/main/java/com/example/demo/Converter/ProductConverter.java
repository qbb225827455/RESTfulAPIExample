package com.example.demo.Converter;

import com.example.demo.Model.Product;
import com.example.demo.Model.ProductRequest;

public class ProductConverter {

    private ProductConverter() {
    }

    public static Product convertToProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }
}
