--liquibase formatted sql
--changeset benatti:create-cinema-hall-table splitStatements:true endDelimiter:;
CREATE TABLE `cinema_halls` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `capacity` int NOT NULL,
                                `description` varchar(255) DEFAULT NULL,
                                PRIMARY KEY (`id`)
);
--rollback DROP TABLE users
