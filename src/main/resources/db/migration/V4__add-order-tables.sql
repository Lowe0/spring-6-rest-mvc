drop table if exists beer_order_line;
drop table if exists beer_order;

create table beer_order (
    id varchar(36) not null,
    created_date datetime(6) not null,
    customer_ref varchar(255),
    last_modified_date datetime(6),
    version bigint,
    customer_id varchar(36) not null,
    primary key (id),
    constraint fk_beer_order_customer foreign key (customer_id) references customer(id)
);

create table beer_order_line (
    id varchar(36) not null,
    beer_id varchar(36),
    created_date datetime(6) not null,
    last_modified_date datetime(6),
    order_quantity int not null,
    quantity_allocated int not null,
    version bigint,
    beer_order_id varchar(36) not null,
    primary key (id),
    constraint fk_beer_order_line_beer_order foreign key (beer_order_id) references beer_order(id),
    constraint fk_beer_order_line_beer foreign key (beer_id) references beer(id)
);
