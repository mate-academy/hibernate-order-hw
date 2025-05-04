package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class UserDaoImpl implements UserDao {
    private final Session session = HibernateUtil.getSessionFactory().openSession();

    @Override
    public User add(User user) {

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert a user: " + user, e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Query<User> query = session.createQuery("FROM User "
                + "WHERE email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResultOptional();
    }
}
