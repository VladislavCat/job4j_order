package ru.job4j.order.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import ru.job4j.order.model.Order;
import ru.job4j.order.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class DishRepository {
    private final String url = "localhost:8080/dish";
    @Autowired
    private final RestTemplate client = new RestTemplate();

    public List<Product> findAllProductByOrder(Order order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Optional<String> s = order
                .getSet()
                .stream()
                .map(Product::getId)
                .map(String::valueOf)
                .reduce((id1, id2) -> id1 + ", " + id2);
        String requestBody = "{\"ids\":\"[" + s.get() + "]\"";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        return client.exchange(url + "/findDishesForOrder", HttpMethod.GET, request,
                new ParameterizedTypeReference<List<Product>>() {
        }).getBody();
    }
}
