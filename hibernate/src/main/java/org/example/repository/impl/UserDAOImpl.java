package org.example.repository.impl;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.repository.UserDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Override
    public User findById(Long id) {
        Transaction tx;
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            user = session.get(User.class, id);
            tx.commit();
        } catch (Exception e) {
            log.error("Failed to find user with id {}", id);
        }
        return user;
    }

    @Override
    public void save(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            log.info("User saved: {}", user);
        } catch (HibernateException e) {
            log.error("Hibernate error: {}", e.getMessage());
        } catch (PersistenceException e) {
            handleConstraintViolation(e);
        } catch (Exception e) {
            log.error("Unexpected error");
        } finally {
            rollbackIsActive(tx);
            closeSession(session);
        }
    }

    @Override
    public void update(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            log.info("User updated: {}", user);
        } catch (ConstraintViolationException e) {
            handleConstraintViolation(e);
        } catch (HibernateException e) {
            log.error("Hibernate error on update: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error on update: {}", e.getMessage());
        } finally {
            rollbackIsActive(tx);
            closeSession(session);
        }
    }

    @Override
    public void delete(Long id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            tx.commit();
        } catch (ConstraintViolationException e) {
            handleConstraintViolation(e);
        } catch (HibernateException e) {
            log.error("Hibernate error on delete: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error on delete: {}", e.getMessage());
        } finally {
            rollbackIsActive(tx);
            closeSession(session);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            log.error("Failed to fetch users");
            return Collections.emptyList();
        }
    }

    private void rollbackIsActive(Transaction tx) {
        if (tx != null && tx.isActive()) try {
            tx.rollback();
        } catch (Exception e) {
            log.error("Failed to rollback");
        }
    }

    private void closeSession(Session session) {
        if (session != null && session.isOpen()) session.close();
    }

    private void handleConstraintViolation(PersistenceException e) { //fixme: переделать на sout, изза SQLExceptionHelper
        SQLException sqlEx = (SQLException) e.getCause().getCause();
        String sqlState = sqlEx.getSQLState();

        switch (sqlState) {
            case "23505":
                log.error("Unique constraint violated: {}", sqlEx.getMessage());
                break;
            case "23502":
                log.error("Not-null constraint violated: {}", sqlEx.getMessage());
                break;
            default:
                log.error("Constraint violation: {}", sqlEx.getMessage());
        }
    }
}
