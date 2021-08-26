--liquibase formatted sql
--changeset dkhromenko:create-order-table splitStatements:true endDelimiter:;
CREATE TABLE orders (id BIGINT NOT NULL AUTO_INCREMENT, orderDate DATETIME(6), user_id BIGINT, PRIMARY KEY (id)) ENGINE=InnoDB;
CREATE TABLE orders_tickets (Order_id BIGINT NOT NULL, tickets_id BIGINT NOT NULL) ENGINE=InnoDB;

--rollback DROP TABLE orders;
