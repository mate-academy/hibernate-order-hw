--liquibase formatted sql
--changeset markg:3 splitStatements:true endDelimiter:;
ALTER TABLE shopping_carts
RENAME COLUMN user_id to id;