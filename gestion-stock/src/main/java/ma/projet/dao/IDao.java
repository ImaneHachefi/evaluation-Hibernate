package ma.projet.dao;

import java.io.Serializable;
import java.util.List;

public interface IDao<T> {
    boolean create(T o);
    boolean update(T o);
    boolean delete(T o);
    T getById(Serializable id);
    List<T> getAll();
}
