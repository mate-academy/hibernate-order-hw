--liquibase formatted sql
--changeset benatti:create-ticket-table splitStatements:true endDelimiter:;
CREATE TABLE `tickets` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `movie_session_id` bigint DEFAULT NULL,
                           `user_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKidxabarcn97kf9acrcqokf1r9` (`movie_session_id`),
                           KEY `FK4eqsebpimnjen0q46ja6fl2hl` (`user_id`),
                           CONSTRAINT `FK4eqsebpimnjen0q46ja6fl2hl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                           CONSTRAINT `FKidxabarcn97kf9acrcqokf1r9` FOREIGN KEY (`movie_session_id`) REFERENCES `movie_sessions` (`id`)
);
--rollback DROP TABLE users
