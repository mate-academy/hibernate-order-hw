--liquibase formatted sql
--changeset markg:2 splitStatements:true endDelimiter:;
CREATE TABLE orders (
    id bigint NOT NULL AUTO_INCREMENT,
    order_date datetime(6) DEFAULT NULL,
    user_id bigint DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Orders_User FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE orders_tickets (
    order_id bigint NOT NULL,
    ticket_id bigint NOT NULL,
    CONSTRAINT FK_Order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT FK_Ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id)
)