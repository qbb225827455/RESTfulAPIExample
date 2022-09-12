package com.example.demo.Exception;

public class UnprocessableEntity extends RuntimeException {

    public UnprocessableEntity(String msg) {
        super(msg);
    }
}
