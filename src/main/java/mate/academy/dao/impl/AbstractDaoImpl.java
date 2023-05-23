package mate.academy.dao.impl;

import mate.academy.dao.AbstractDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {
    @Override
    public T add(T object) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();
            return object;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    String.format("Can't insert %s ", object.getClass()), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
