CREATE TABLE `cinema_halls` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `capacity` int NOT NULL,
                                `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `halls` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `capacity` int NOT NULL,
                         `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `movie_sessions` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `showTime` datetime DEFAULT NULL,
                                  `cinemaHall_id` bigint DEFAULT NULL,
                                  `movie_id` bigint DEFAULT NULL,
                                  PRIMARY KEY (`id`),
    KEY `FKk04umq7ulfxwqtgoqf59a0reu` (`cinemaHall_id`),
    KEY `FKatpmn0h31nwhwdgd0ogr8q6kj` (`movie_id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `movies` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `dateTime` datetime DEFAULT NULL,
                          `user_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`id`),
    KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `sessions` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `sessionTime` datetime DEFAULT NULL,
                            `cinemaHall_id` bigint DEFAULT NULL,
                            `movie_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
    KEY `FKf0wrhor1n338i8mk5wdmdc5rb` (`cinemaHall_id`),
    KEY `FKcjgs3ugow5xk78mt9lo5o1di6` (`movie_id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `shopping_carts` (
                                  `id` bigint NOT NULL,
                                  PRIMARY KEY (`id`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `tickets` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `movieSession_id` bigint DEFAULT NULL,
                           `user_id` bigint DEFAULT NULL,
                           `cart_id` bigint DEFAULT NULL,
                           `order_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
    KEY `FKn0te1u1cy0eie26t9brwotyub` (`movieSession_id`),
    KEY `FK4eqsebpimnjen0q46ja6fl2hl` (`user_id`),
    KEY `FKr4ja7nb9i1adgtjylhkjwsk59` (`cart_id`),
    KEY `order_id` (`order_id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `salt` tinyblob,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
    ) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
