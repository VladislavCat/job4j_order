package ru.job4j.order.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.order.model.Order;

import java.util.Optional;

@Repository
public interface OrderRepository
        extends CrudRepository<Order, Integer> {
    Optional<Order> save(Order order);
    @Query(value = "select o.id, o.description, p.id, p.name, p.description\n"
            + "from order_products as op\n"
            + "left join table_order as o\n"
            + "on op.id_order=o.id\n"
            + "left join products as p\n"
            + "on op.id_product=p.id where o.id=:id", nativeQuery = true)
    Optional<Order> find(@Param("id") int id);
}