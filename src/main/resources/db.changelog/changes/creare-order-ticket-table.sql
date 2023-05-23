--liquibase formatted sql
--changeset shmax:create-order-table splitStatements:true endDelimiter:;
CREATE TABLE `orders_tickets` (
                          `order_id` BIGINT NOT NULL,
                          `ticket_id` BIGINT NOT NULL,
                          PRIMARY KEY (`order_id`, `ticket_id`),
                          FOREIGN KEY (`order_id`) REFERENCES orders(id),
                          FOREIGN KEY (`ticket_id`) REFERENCES ticket(id)
);

--rollback DROP TABLE `orders_tickets`

