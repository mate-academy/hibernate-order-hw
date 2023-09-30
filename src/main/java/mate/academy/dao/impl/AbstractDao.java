package mate.academy.dao.impl;

import java.util.function.Function;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;

public class AbstractDao {
    protected <T> T performReturningWithinTx(Function<Session, T> sessionFunction) {
        return HibernateUtil.getSessionFactory().fromTransaction(sessionFunction);
    }

    protected <T> T performReturningWithoutTx(Function<Session, T> sessionFunction) {
        return HibernateUtil.getSessionFactory().fromSession(sessionFunction);
    }
}
