--liquibase formatted sql
--changeset benatti:create-shopping_cart_ticket-table splitStatements:true endDelimiter:;
CREATE TABLE `shopping_carts_tickets` (
                                          `shopping_cart_id` bigint NOT NULL,
                                          `ticket_id` bigint NOT NULL,
                                          UNIQUE KEY `UK_eusu1u2g6d73fcjvox629ct9y` (`ticket_id`),
                                          KEY `FKm5n3i4h3nk1p1yqywkn1kpej6` (`shopping_cart_id`),
                                          CONSTRAINT `FKaw8rn6oivawiyro857w06we16` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`),
                                          CONSTRAINT `FKm5n3i4h3nk1p1yqywkn1kpej6` FOREIGN KEY (`shopping_cart_id`) REFERENCES `shopping_carts` (`id`)
);
--rollback DROP TABLE users
