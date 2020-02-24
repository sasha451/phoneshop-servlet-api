package com.es.phoneshop.exceptions;

public class OutOfStockException extends RuntimeException {
    private String message;

    public OutOfStockException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
