package org.example.repository.impl;

import org.example.config.HibernateUtils;
import org.hibernate.Session;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class AbstractRepositoryImpl<E, I>
//        implements AbstractRepository<E, I>
{
    Session session = HibernateUtils.getSessionFactory().openSession();
    private final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractRepositoryImpl() {
        this.entityClass = (Class<E>) ((ParameterizedType)
                getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    //    @Override
    public E findById(I id) {
        return session.get(entityClass, id);
    }

    //    @Override
    public List<E> findAll() {
        return session.createQuery("from " + entityClass.getName()).list();
    }

    //    @Override
    public void save(E entity) {
        session.save(entity);
    }

    //    @Override
    public void delete(I id) {
    }
}
