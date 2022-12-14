package com.example.demo.Service;

import com.example.demo.Auth.UserIdentity;
import com.example.demo.Converter.ProductConverter;
import com.example.demo.Exception.NotFound;
import com.example.demo.Model.Product.Product;
import com.example.demo.Model.ProductQueryParameter;
import com.example.demo.Model.Product.ProductRequest;
import com.example.demo.Model.Product.ProductResponse;
import com.example.demo.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserIdentity userIdentity;

    public Product getProduct(String id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new NotFound("Can't find product."));
    }

    public ProductResponse getProductResponse(String id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new NotFound("Can't find product."));
        return ProductConverter.ConvertToProductResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product product = ProductConverter.convertToProduct(request);
        product.setCreator(userIdentity.getId());
        product = productRepo.insert(product);

        return ProductConverter.ConvertToProductResponse(product);
    }

    public ProductResponse replaceProduct(String id, ProductRequest request) {

        Product oProduct = getProduct(id);

        Product product = ProductConverter.convertToProduct(request);
        product.setId(oProduct.getId());
        product.setCreator(oProduct.getCreator());

        productRepo.save(product);

        return ProductConverter.ConvertToProductResponse(product);
    }

    public void deleteProduct(String id) {

        productRepo.deleteById(id);
    }

    public List<ProductResponse> getProducts(ProductQueryParameter parameter) {

        String keyword = Optional.ofNullable(parameter.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(parameter.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(parameter.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = genSort(parameter.getOrderBy(), parameter.getSortRule());

        List<Product> products = productRepo.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, keyword, sort);

        return products.stream()
                .map(ProductConverter::ConvertToProductResponse)
                .collect(Collectors.toList());
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
