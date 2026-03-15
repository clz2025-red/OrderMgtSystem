package com.ordermgtsystem.dto;

import java.math.BigDecimal;

public record ProductListItemResponse(
        Long productId,
        String productName,
        BigDecimal price,
        Integer stockQuantity
) {
}
