create table if not exists order_products(
    id serial primary key,
    id_order int references table_order(id),
    id_product int references products(id)
)