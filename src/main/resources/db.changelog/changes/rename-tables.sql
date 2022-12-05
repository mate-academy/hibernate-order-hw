--liquibase formatted sql
--changeset shmax:create-order-table splitStatements:true endDelimiter:;
RENAME TABLE
    cinemahall TO cinema_halls,
    movie TO movies,
    moviesession TO movie_sessions,
    shoppingcart TO shopping_carts,
    shoppingcart_ticket TO shopping_carts_tickets,
    ticket TO tickets,
    user TO users