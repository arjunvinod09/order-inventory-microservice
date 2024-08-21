package com.ust.orderservice.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InventoryServiceDto(
        String skuCode,
        boolean available,
        double price
) {
}
