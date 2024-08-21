package com.ust.orderservice.feign_client;

import com.ust.orderservice.payload.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory",url = "http://localhost:8100/products")
public interface InventoryClientService {

    @GetMapping
    ResponseDto isProductAvailable(@RequestParam String skuCode, @RequestParam int quantity);
}
