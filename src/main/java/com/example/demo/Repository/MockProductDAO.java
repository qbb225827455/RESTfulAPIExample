package com.example.demo.Repository;

import com.example.demo.Model.Product;
import com.example.demo.Model.ProductQueryParameter;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MockProductDAO {

    private final List<Product> productList = new ArrayList<>();

    public Product insert(Product product) {
        productList.add(product);
        return product;
    }

    public Product replace(String id, Product product) {
        Optional<Product> productOptional = find(id);
        productOptional.ifPresent(p -> {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
        });

        return product;
    }

    public void delete(String id) {
        productList.removeIf(p -> p.getId().equals(id));
    }

    public Optional<Product> find(String id) {
        return productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> find(ProductQueryParameter param) {
        String keyword = Optional.ofNullable(param.getKeyword()).orElse("");
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();
        Comparator<Product> comparator = genSortComparator(orderBy, sortRule);

        return productList.stream()
                .filter(p -> p.getName().contains(keyword))
                .sorted(comparator)
                .collect(Collectors.toList());
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
