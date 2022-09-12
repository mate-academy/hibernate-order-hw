--liquibase formatted sql
--changeset benatti:create-shopping-cart-table splitStatements:true endDelimiter:;
CREATE TABLE `shopping_carts` (
                                  `id` bigint NOT NULL,
                                  PRIMARY KEY (`id`),
                                  CONSTRAINT `FKc1fbrvff059ke4p8ce3hu38oa` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
);
--rollback DROP TABLE users
