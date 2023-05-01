package ru.job4j.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.job4j.order.Main;
import ru.job4j.order.model.Order;
import ru.job4j.order.model.OrderDto;
import ru.job4j.order.service.DishService;
import ru.job4j.order.service.OrderService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class RestOrderController {
    private final OrderService service;
    private final DishService dishService;
    private final KafkaTemplate<Object, Order> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/createNewOrder")
    public ResponseEntity<String> newOrder(@RequestBody Map<String, String> body) throws JsonProcessingException {
        Order order = new Order();
        order.setDescriptionOrder(body.get("description"));
        Optional<Order> opt = service.save(order);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        kafkaTemplate.send("job4j_orders",
                opt.get());
        return ResponseEntity.ok("Номер вашего заказа " + order.getId());
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
