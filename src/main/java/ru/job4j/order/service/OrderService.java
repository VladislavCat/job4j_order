package ru.job4j.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.order.model.Order;
import ru.job4j.order.repository.OrderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findById(int id) {
        return orderRepository.find(id);
    }
}
