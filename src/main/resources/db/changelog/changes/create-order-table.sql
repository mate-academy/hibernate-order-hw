--liquibase formatted sql
--changeset benatti:create-order-table splitStatements:true endDelimiter:;
CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `order_date` datetime(6) DEFAULT NULL,
                          `user_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
                          CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);
--rollback DROP TABLE users
