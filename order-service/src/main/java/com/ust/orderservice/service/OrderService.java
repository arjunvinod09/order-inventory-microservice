package com.ust.orderservice.service;

import com.ust.orderservice.domain.Order;
import com.ust.orderservice.domain.OrderItem;
import com.ust.orderservice.feign_client.InventoryClientService;
import com.ust.orderservice.payload.InventoryServiceDto;
import com.ust.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryClientService inventoryClientService;

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        order = orderRepository.save(order);
        return order;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public boolean validOrder(Order order){
        double totalPrice = 0.0;
        boolean flag = true;
        for(OrderItem orderItem : order.getOrderItems()){
            InventoryServiceDto inventoryServiceDto = inventoryClientService.isProductAvailable(orderItem.getSkuCode(),orderItem.getQuantity());
            if(inventoryServiceDto.available()){
                totalPrice = totalPrice + (inventoryServiceDto.price() * orderItem.getQuantity());
            }
            else{
                flag = false;
            }
            order.setTotalPrice(totalPrice);
        }
        return flag;
    }
}

/*
    foreach product retreived
        if(! retreived.available)
            flag = false
        productDtos.add(new productDtos(retreived.skucode , order.quantity, retreived.price))
    new ResponeDto(productDtos , "Success or failure based on flag")
     */