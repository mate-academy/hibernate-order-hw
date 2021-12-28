package mate.academy.dao.impl;

import mate.academy.util.HibernateUtil;
import org.hibernate.SessionFactory;

public abstract class AbstractDao {
    protected SessionFactory factory;

    protected AbstractDao() {
        this.factory = HibernateUtil.getSessionFactory();
    }
}
