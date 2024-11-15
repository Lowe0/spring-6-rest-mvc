drop table if exists category;
drop table if exists beer_category;

create table category
(
    id                 varchar(36) not null,
    version            bigint,
    created_date       datetime(6) not null,
    last_modified_date datetime(6),
    description        varchar(50),
    primary key (id)
);

create table beer_category
(
    beer_id     varchar(36) not null,
    category_id varchar(36) not null,
    primary key (beer_id, category_id),
    constraint fk_beer_category_to_beer foreign key (beer_id) references beer (id),
    constraint fk_beer_category_to_category foreign key (category_id) references category (id)
);