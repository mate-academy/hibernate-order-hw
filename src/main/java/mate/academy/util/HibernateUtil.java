package mate.academy.util;

import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = initSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory initSessionFactory() {
        try {
            return new Configuration()
                    .configure()
                    .addAnnotatedClass(Movie.class)
                    .addAnnotatedClass(CinemaHall.class)
                    .addAnnotatedClass(MovieSession.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Ticket.class)
                    .addAnnotatedClass(ShoppingCart.class)
                    .addAnnotatedClass(Order.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error creating SessionFactory", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
