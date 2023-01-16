create table customer (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name varchar(255),
                          tier integer
);

create table product (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         category varchar(255),
                         name varchar(255),
                         price double
);

--User is reserved word
--Order is reserved word
create table order_customer (
                               id INTEGER PRIMARY KEY AUTOINCREMENT,
                               order_date varchar(255),
                               delivery_date varchar(255),
                               status varchar(255),
                               customer_id bigint,
                               FOREIGN KEY (customer_id) REFERENCES customer (id)
);

create table order_product_relationship (
                                            order_id bigint not null,
                                            product_id bigint not null,
                                            primary key (order_id, product_id),
                                            FOREIGN KEY (order_id) REFERENCES order_customer (id),
                                            FOREIGN KEY (product_id) REFERENCES product (id)
);