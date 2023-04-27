package ru.job4j.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.order.model.Order;
import ru.job4j.order.model.OrderDto;
import ru.job4j.order.repository.DishRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository repository;

    public List<OrderDto.ProductDto> findProductsByOrder(Order order) {
        return repository.findAllProductByOrder(order)
                .stream()
                .map(product -> new OrderDto.ProductDto(product.getId(), product.getName(), product.getDescription()))
                .collect(Collectors.toList());
    }
}
