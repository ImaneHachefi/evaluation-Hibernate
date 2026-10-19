package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/** Implémentation générique commune à tous les services. */
public abstract class AbstractService<T> implements IDao<T> {

    private final Class<T> type;

    protected AbstractService(Class<T> type) {
        this.type = type;
    }

    private boolean execute(Consumer<Session> action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean create(T o) { return execute(s -> s.save(o)); }

    @Override
    public boolean update(T o) { return execute(s -> s.update(o)); }

    @Override
    public boolean delete(T o) { return execute(s -> s.delete(o)); }

    @Override
    public T getById(Serializable id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(type, id);
        }
    }

    @Override
    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + type.getSimpleName(), type).list();
        }
    }
}
