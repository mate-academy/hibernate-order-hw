--liquibase formatted sql
--changeset shmax:create-order-table splitStatements:true endDelimiter:;
CREATE TABLE `orders` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `orderDate` DATE NOT NULL,
                          `user_id` BIGINT NOT NULL,
                          PRIMARY KEY (`id`),
                          FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

--rollback DROP TABLE `orders`
