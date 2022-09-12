package com.example.demo.Controller;

import com.example.demo.Model.Product;
import com.example.demo.Model.ProductQueryParameter;
import com.example.demo.Service.ProductService;
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
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {

        Product product = productService.getProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @ModelAttribute ProductQueryParameter parameter) {

        List<Product> products = productService.getProducts(parameter);
        return ResponseEntity.ok().body(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product request) {

        Product product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return  ResponseEntity.created(location).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id, @Valid @RequestBody Product request) {

        Product product = productService.replaceProduct(id, request);
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
