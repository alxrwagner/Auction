-- liquibase formatted sql

-- changeset wagner:create-table-lot
CREATE TABLE lot(
    id BIGSERIAL  NOT NULL PRIMARY KEY,
    status text not null ,
    title text not null,
    description TEXT NOT NULL,
    start_price INTEGER NOT NULL,
    bid_price INTEGER NOT NULL
);