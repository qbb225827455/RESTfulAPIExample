package com.example.demo.Model;

import lombok.Data;

@Data
public class ProductQueryParameter {
    private String keyword;
    private String orderBy;
    private String sortRule;
}
