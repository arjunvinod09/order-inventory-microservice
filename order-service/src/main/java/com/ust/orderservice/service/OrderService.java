package com.ust.orderservice.service;

import com.ust.orderservice.domain.Order;
import com.ust.orderservice.domain.OrderItem;
import com.ust.orderservice.feign_client.InventoryClientService;
import com.ust.orderservice.payload.InventoryServiceDto;
import com.ust.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final InventoryClientService inventoryClientService;

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        for(var orderItem : order.getOrderItems()){
            order.setTotalPrice(order.getTotalPrice() + (orderItem.getPrice()*orderItem.getQuantity()));
        }
        order = orderRepository.save(order);
        return order;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public boolean validOrder(Order order){
        double totalPrice = 0.0;
        boolean flag = true;
        for(OrderItem orderItem : order.getOrderItems()) {
            InventoryServiceDto inventoryServiceDto = inventoryClientService.isProductAvailable(orderItem.getSkuCode(), orderItem.getQuantity());
            if (inventoryServiceDto.available()) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }
}