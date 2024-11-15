drop table if exists beer_order_shipment;

create table beer_order_shipment
(

    id                 varchar(36) not null,
    version            bigint,
    created_date       datetime(6) not null,
    last_modified_date datetime(6),
    tracking_number    varchar(50),
    beer_order_id      varchar(36),
    primary key (id),
    constraint fk_beer_order_shipment_to_beer_order foreign key (beer_order_id) references beer_order (id)
);

alter table beer_order
    add column beer_order_shipment_id varchar(36);

alter table beer_order
    add constraint fk_beer_order_to_beer_order_shipment foreign key (beer_order_shipment_id) references beer_order_shipment (id);