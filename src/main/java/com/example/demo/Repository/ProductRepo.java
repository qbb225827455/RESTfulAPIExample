package com.example.demo.Repository;

import com.example.demo.Model.Product.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {

    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom, int priceTo, String name, Sort sort);
}
