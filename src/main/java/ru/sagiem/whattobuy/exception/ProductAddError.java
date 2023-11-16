package ru.sagiem.whattobuy.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ProductAddError {
    private int status;
    private String message;
    private Date timestamp;

    public ProductAddError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}