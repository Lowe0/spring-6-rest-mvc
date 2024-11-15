drop table if exists beer;
drop table if exists customer;
create table beer
(
    id                 varchar(36)    not null,
    version            integer,
    created_date       datetime(6) not null,
    last_modified_date datetime(6),
    beer_style         tinyint        not null check (beer_style between 0 and 8),
    price              decimal(38, 2) not null,
    quantity_on_hand   integer,
    beer_name          varchar(50)    not null,
    upc                varchar(255)   not null,
    primary key (id)
) engine = InnoDB;
create table customer
(
    id                 varchar(36) not null,
    version            integer,
    created_date       datetime(6) not null,
    last_modified_date datetime(6),
    customer_name      varchar(255),
    email              varchar(255),
    primary key (id)
) engine = InnoDB;