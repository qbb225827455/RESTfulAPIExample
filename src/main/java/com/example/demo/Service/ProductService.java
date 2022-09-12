package com.example.demo.Service;

import com.example.demo.Exception.NotFound;
import com.example.demo.Model.Product;
import com.example.demo.Model.ProductQueryParameter;
import com.example.demo.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Product createProduct(Product request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productRepo.insert(product);
    }

    public Product getProduct(String id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new NotFound("Can't find product."));
    }

    public Product replaceProduct(String id, Product request) {

        Product oProduct = getProduct(id);

        Product product = new Product();
        product.setId(oProduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productRepo.save(product);
    }

    public void deleteProduct(String id) {

        productRepo.deleteById(id);
    }

    public List<Product> getProducts(ProductQueryParameter parameter) {

        String keyword = Optional.ofNullable(parameter.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(parameter.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(parameter.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = genSort(parameter.getOrderBy(), parameter.getSortRule());

        return productRepo.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, keyword, sort);
    }

    private Sort genSort(String orderBy, String sortRule) {

        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) || Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
        }

        return sort;
    }
}
