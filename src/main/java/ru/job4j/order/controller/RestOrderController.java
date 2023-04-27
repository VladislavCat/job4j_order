package ru.job4j.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.order.model.Order;
import ru.job4j.order.model.OrderDto;
import ru.job4j.order.service.DishService;
import ru.job4j.order.service.OrderService;

import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class RestOrderController {
    private final OrderService service;
    private final DishService dishService;

    @PostMapping("/createNewOrder")
    public ResponseEntity<String> newOrder(@RequestBody String description) {
        Order order = new Order();
        order.setDescriptionOrder(description);
        Optional<Order> opt = service.save(order);
        return opt.isPresent() ? ResponseEntity.ok("Номер вашего заказа" + order.getId())
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/findOrderById/{orderId}")
    public ResponseEntity<Order> findOrder(@PathVariable("orderId") int id) {
        Optional<Order> order = service.findById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDto> findOrderDto(@PathVariable("id") int id) {
        Optional<Order> order = service.findById(id);
        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        OrderDto orderDto = new OrderDto(order.get().getId(),
                order.get().getDescriptionOrder(), new HashSet<>(dishService.findProductsByOrder(order.get())));
        return ResponseEntity.ok(orderDto);
    }
}
