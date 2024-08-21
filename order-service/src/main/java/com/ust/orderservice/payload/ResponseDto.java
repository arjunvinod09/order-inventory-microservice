package com.ust.orderservice.payload;

import java.util.List;

public record ResponseDto(
    List<ProductDto> productDtos,
    Status status
) {
}
