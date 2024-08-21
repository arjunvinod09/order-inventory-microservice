package com.ust.orderservice.payload;

public record ProductDto(
        String skuCode,
        int quantity,
        double price
) {
}
