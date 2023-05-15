package ru.job4j.order.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class OrderDto implements Serializable {
    private final int id;
    private final String descriptionOrder;
    private final String statusOrder;
    private final Set<ProductDto> set;

    @Data
    public static class ProductDto implements Serializable {
        private final int id;
        private final String name;
        private final String description;
    }
}
