package com.example.demo.Service;

import com.example.demo.Exception.NotFound;
import com.example.demo.Exception.UnprocessableEntity;
import com.example.demo.Model.Product;
import com.example.demo.Model.ProductQueryParameter;
import com.example.demo.Repository.MockProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private MockProductDAO productDAO;

    public Product createProduct(Product request) {

        boolean isIdExisted = productDAO.find(request.getId()).isPresent();
        if (isIdExisted) {
            throw new UnprocessableEntity("The id of the product is duplicated.");
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productDAO.insert(product);
    }

    public Product getProduct(String id) {
        return productDAO.find(id)
                .orElseThrow(() -> new NotFound("Can't find product."));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productDAO.replace(product.getId(), request);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product.getId());
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        return productDAO.find(param);
    }
}
