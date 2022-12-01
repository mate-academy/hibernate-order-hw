package mate.academy.dao.impl;

import mate.academy.dao.GenericDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GenericDaoImpl<T> implements GenericDao<T> {
    @Override
    public T add(T t) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(t);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert entity: " + t, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
