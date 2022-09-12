--liquibase formatted sql
--changeset benatti:create-movie-session-table splitStatements:true endDelimiter:;
CREATE TABLE `movie_sessions` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `show_time` datetime(6) DEFAULT NULL,
                                  `cinema_hall_id` bigint DEFAULT NULL,
                                  `movie_id` bigint DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `FKfqu527x0e0675k7vkm21ghv08` (`cinema_hall_id`),
                                  KEY `FKatpmn0h31nwhwdgd0ogr8q6kj` (`movie_id`),
                                  CONSTRAINT `FKatpmn0h31nwhwdgd0ogr8q6kj` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
                                  CONSTRAINT `FKfqu527x0e0675k7vkm21ghv08` FOREIGN KEY (`cinema_hall_id`) REFERENCES `cinema_halls` (`id`)
);
--rollback DROP TABLE users
