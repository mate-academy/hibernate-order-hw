--liquibase formatted sql
--changeset markg:1 splitStatements:true endDelimiter:;
CREATE DATABASE  IF NOT EXISTS `cinema`;
USE `cinema`;

--
-- Table structure for table `cinema_halls`
--

DROP TABLE IF EXISTS `cinema_halls`;
CREATE TABLE `cinema_halls` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `capacity` int NOT NULL,
                                `description` varchar(255) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
CREATE TABLE `movies` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `description` varchar(255) DEFAULT NULL,
                          `title` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `movie_sessions`
--

DROP TABLE IF EXISTS `movie_sessions`;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `email` varchar(255) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `salt` tinyblob,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
--
-- Table structure for table `shopping_carts`
--

DROP TABLE IF EXISTS `shopping_carts`;
CREATE TABLE `shopping_carts` (
                                  `user_id` bigint NOT NULL,
                                  PRIMARY KEY (`user_id`),
                                  CONSTRAINT `FK3iw2988ea60alsp0gnvvyt744` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
CREATE TABLE `tickets` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `movie_session_id` bigint DEFAULT NULL,
                           `user_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKidxabarcn97kf9acrcqokf1r9` (`movie_session_id`),
                           KEY `FK4eqsebpimnjen0q46ja6fl2hl` (`user_id`),
                           CONSTRAINT `FK4eqsebpimnjen0q46ja6fl2hl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                           CONSTRAINT `FKidxabarcn97kf9acrcqokf1r9` FOREIGN KEY (`movie_session_id`) REFERENCES `movie_sessions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `shopping_carts_tickets`
--

DROP TABLE IF EXISTS `shopping_carts_tickets`;
CREATE TABLE `shopping_carts_tickets` (
                                          `shopping_cart_id` bigint NOT NULL,
                                          `ticket_id` bigint NOT NULL,
                                          UNIQUE KEY `UK_eusu1u2g6d73fcjvox629ct9y` (`ticket_id`),
                                          KEY `FKm5n3i4h3nk1p1yqywkn1kpej6` (`shopping_cart_id`),
                                          CONSTRAINT `FKaw8rn6oivawiyro857w06we16` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`),
                                          CONSTRAINT `FKm5n3i4h3nk1p1yqywkn1kpej6` FOREIGN KEY (`shopping_cart_id`) REFERENCES `shopping_carts` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

