package com.ust.orderservice.controller;

import com.ust.orderservice.domain.Order;
import com.ust.orderservice.domain.OrderItem;
import com.ust.orderservice.domain.OrderStatus;
import com.ust.orderservice.feign_client.InventoryClientService;
import com.ust.orderservice.payload.OrderRequest;
import com.ust.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    InventoryClientService inventoryClientService;
    @Autowired
    OrderService orderService;

    // POST /orders create order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setOrderItems(orderRequest.orderItems().stream().map(orderItem -> {
            OrderItem item = new OrderItem();
            item.setSkuCode(orderItem.skuCode());
            item.setQuantity(orderItem.quantity());
            item.setPrice(inventoryClientService.getProduct(orderItem.skuCode()).price());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    // GET /orders/{id} get order by id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable long id){
        var fetchedOrder = orderService.getOrderById(id);
        var valid = orderService.validOrder(fetchedOrder);
        if(valid){
            fetchedOrder.setStatus(OrderStatus.CONFIRMED);
            return ResponseEntity.ok(orderService.createOrder(orderService.getOrderById(id)));
        }
        else{
            return ResponseEntity.ok(fetchedOrder);
        }
    }
}
