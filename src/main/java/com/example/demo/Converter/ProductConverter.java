package com.example.demo.Converter;

import com.example.demo.Model.Product;
import com.example.demo.Model.ProductRequest;
import com.example.demo.Model.ProductResponse;

public class ProductConverter {

    private ProductConverter() {
    }

    public static Product convertToProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }

    public static ProductResponse ConvertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;
    }
}
