alter table beer modify column beer_style tinyint not null check (beer_style between 0 and 8);