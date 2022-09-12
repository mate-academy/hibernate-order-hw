--liquibase formatted sql
--changeset benatti:create-movie-table splitStatements:true endDelimiter:;
CREATE TABLE `movies` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `description` varchar(255) DEFAULT NULL,
                          `title` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
);
--rollback DROP TABLE users
