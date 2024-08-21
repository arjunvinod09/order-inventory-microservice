package com.ust.orderservice.feign_client;

import com.ust.orderservice.payload.InventoryServiceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory",url = "http://localhost:8100/products")
public interface InventoryClientService {
    @GetMapping
    InventoryServiceDto isProductAvailable(@RequestParam String skuCode, @RequestParam int quantity);
    @GetMapping("/{skuCode}")
    InventoryServiceDto getProduct(@PathVariable String skuCode);
}
