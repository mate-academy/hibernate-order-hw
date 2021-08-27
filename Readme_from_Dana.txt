Если ты вдруг будешь тестировать эту задачу,
тебе пригодится код для удаления, так как я
реализовала не create-drop, а validate.
Но ты, конечно, можешь просто изменить это
в hibernate.cfg.xml. Но на всякий случай:

DELETE FROM ticket_app.shopping_carts_tickets;
DELETE FROM ticket_app.orders_tickets;
DELETE FROM ticket_app.orders;
DELETE FROM ticket_app.shopping_carts;
DELETE FROM ticket_app.tickets;
DELETE FROM ticket_app.movie_sessions;
DELETE FROM ticket_app.cinema_halls;
DELETE FROM ticket_app.movies;
DELETE FROM ticket_app.users;
