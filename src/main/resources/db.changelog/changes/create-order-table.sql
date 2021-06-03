--liquibase formatted sql
--changeset mykolamanko:create-order-table splitStatements:true endDelimiter:;
CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `dateTime` datetime DEFAULT NULL,
                          `user_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`id`),
    KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE tickets add column order_id bigint default null;
ALTER TABLE tickets ADD FOREIGN KEY (order_id) REFERENCES orders(id);

--rollback DROP TABLE orders;