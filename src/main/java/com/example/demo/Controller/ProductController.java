package com.example.demo.Controller;

import com.example.demo.Model.Product.Product;
import com.example.demo.Model.ProductQueryParameter;
import com.example.demo.Model.Product.ProductRequest;
import com.example.demo.Model.Product.ProductResponse;
import com.example.demo.Service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    // 透過@Parameter標記，說明路徑參數。
    public ResponseEntity<ProductResponse> getProduct(@Parameter(description = "ID of product.") @PathVariable("id") String id) {

        ProductResponse product = productService.getProductResponse(id);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @ModelAttribute ProductQueryParameter parameter) {

        List<ProductResponse> products = productService.getProducts(parameter);
        return ResponseEntity.ok().body(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {

        ProductResponse product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return  ResponseEntity.created(location).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> replaceProduct(
            @PathVariable("id") String id, @Valid @RequestBody ProductRequest request) {

        ProductResponse product = productService.replaceProduct(id, request);
        return  ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private Comparator<Product> genSortComparator(String orderBy, String sortRule) {

        Comparator<Product> comparator = (p1, p2) -> 0;
        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
            return comparator;
        }

        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Product::getName);
        }

        return sortRule.equalsIgnoreCase("desc")
                ? comparator.reversed()
                : comparator;
    }
}
