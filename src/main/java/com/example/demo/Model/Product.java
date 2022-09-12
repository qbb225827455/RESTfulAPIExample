package com.example.demo.Model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Document(collection = "products")
// 參考
// 驗證資料
// https://ithelp.ithome.com.tw/articles/10275699
public class Product {
    private String id;

    @NotEmpty(message = "Product name is undefined.")
    private String name;

    @Min(value = 0, message = "Price should be greater or equal to 0.")
    private int price;
}
