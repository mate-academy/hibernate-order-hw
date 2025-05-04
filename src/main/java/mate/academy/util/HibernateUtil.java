package mate.academy.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = initSessionFactory();
    private static final String PASSWORD_ENV_ALIAS = "DB_PASS";

    private HibernateUtil() {
    }

    private static SessionFactory initSessionFactory() {
        try {
            Configuration configure = new Configuration().configure();
            configure.getProperties()
                    .setProperty("hibernate.connection.password",
                            System.getenv(PASSWORD_ENV_ALIAS));
            return configure.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error creating SessionFactory", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
