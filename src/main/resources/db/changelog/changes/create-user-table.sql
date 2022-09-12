--liquibase formatted sql
--changeset benatti:create-user-table splitStatements:true endDelimiter:;
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `email` varchar(255) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `salt` tinyblob,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
);
--rollback DROP TABLE users
