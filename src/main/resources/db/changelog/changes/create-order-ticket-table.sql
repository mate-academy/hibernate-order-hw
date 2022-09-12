--liquibase formatted sql
--changeset benatti:create-order-ticket-table splitStatements:true endDelimiter:;
CREATE TABLE `orders_tickets` (
                                  `order_id` bigint NOT NULL,
                                  `ticket_id` bigint NOT NULL,
                                  UNIQUE KEY `UK_4l081u6j1tuvch26evaekjihi` (`ticket_id`),
                                  KEY `FKjoggp5iyq5fqbtxi5r1m2wlty` (`order_id`),
                                  CONSTRAINT `FKhpx6qk3psnomt9t2aig2nunfq` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`),
                                  CONSTRAINT `FKjoggp5iyq5fqbtxi5r1m2wlty` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
);
--rollback DROP TABLE users
