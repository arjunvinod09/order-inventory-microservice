package com.ust.orderservice.service;

import com.ust.orderservice.domain.Order;
import com.ust.orderservice.domain.OrderItem;
import com.ust.orderservice.feign_client.InventoryClientService;
import com.ust.orderservice.payload.InventoryServiceDto;
import com.ust.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final InventoryClientService inventoryClientService;

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        order = orderRepository.save(order);
        return order;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public double validOrder(Order order){
        double totalPrice = 0.0;
        boolean flag = true;
        log.debug("Order received: {}", order);
        for(OrderItem orderItem : order.getOrderItems()){
            log.debug("Getting each order item {} from the order", orderItem);
            InventoryServiceDto inventoryServiceDto = inventoryClientService.isProductAvailable(orderItem.getSkuCode(),orderItem.getQuantity());
            if(inventoryServiceDto.available()){
                log.debug("Getting price for item {} {} and adding it to total price {}", orderItem.getSkuCode(), orderItem.getPrice(), totalPrice);
                totalPrice = totalPrice + (inventoryServiceDto.price() * orderItem.getQuantity());
            }
            else{
                flag = false;
            }
        }
        if(flag){
            return totalPrice;
        }
        else{
            return 0.0;
        }
    }
}