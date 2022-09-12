package com.example.demo.Controller;

import com.example.demo.Model.Product;
import com.example.demo.Model.ProductQueryParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final List<Product> productList = new ArrayList<>();

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {

        Optional<Product> productOptional = productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product request) {

        boolean isIdExisted = productList.stream()
                .anyMatch(p -> p.getId().equals(request.getId()));
        if (isIdExisted) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        productList.add(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return  ResponseEntity.created(location).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id, @RequestBody Product request) {

        Optional<Product> productOptional = productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(request.getName());
            product.setPrice(request.getPrice());

            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {

        boolean isRemoved = productList.removeIf(p -> p.getId().equals(id));

        return isRemoved
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter parameter) {

        String keyword = parameter.getKeyword();
        String orderBy = parameter.getOrderBy();
        String sortRule = parameter.getSortRule();
        Comparator<Product> comparator = genSortComparator(orderBy, sortRule);

        List<Product> products = productList.stream()
                .filter(p -> p.getName().toUpperCase().contains(keyword.toUpperCase()))
                .sorted(comparator)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(products);
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

    @PostConstruct
    private void initDB() {
        Product product1 = new Product();
        product1.setId("A01");
        product1.setName("AAA");
        product1.setPrice(100);

        Product product2 = new Product();
        product2.setId("A02");
        product2.setName("BBB");
        product2.setPrice(100);

        Product product3 = new Product();
        product3.setId("A03");
        product3.setName("CCC");
        product3.setPrice(100);

        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
    }
}
