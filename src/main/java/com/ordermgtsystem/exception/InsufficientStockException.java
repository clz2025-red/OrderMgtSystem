package com.ordermgtsystem.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long productId, Integer currentStock, Integer requestedQuantity) {
        super("Insufficient stock for product id=" + productId
                + ", current=" + currentStock
                + ", requested=" + requestedQuantity);
    }
}