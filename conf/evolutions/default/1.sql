# --- First database schema

# --- !Ups

set ignorecase true;

create table users (
  id                        bigint not null,
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  token                     varchar(255) not null,
  constraint pk_company     primary key (id)
);

# --- !Downs

drop table if exists users;
