-- liquibase formatted sql

-- changeset wagner:create-table-bid
CREATE TABLE bid(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    bidder_name varchar(100) NOT NULL,
    bid_date timestamp WITHOUT TIME ZONE NOT NULL,
    lot_id BIGINT NOT NULL
);