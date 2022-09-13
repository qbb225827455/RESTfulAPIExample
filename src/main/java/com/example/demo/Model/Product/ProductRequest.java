package com.example.demo.Model.Product;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
// 參考
// 驗證資料
// https://ithelp.ithome.com.tw/articles/10275699
public class ProductRequest {

    @NotEmpty(message = "Product name is undefined.")
    private String name;

    @NotNull
    @Min(value = 0, message = "Price should be greater or equal to 0.")
    private Integer price;
}
