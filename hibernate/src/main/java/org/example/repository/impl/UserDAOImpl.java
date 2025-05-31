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
import org.hibernate.exception.SQLGrammarException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Override
    public User findById(Long id) {
        Transaction tx;
        User user;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            user = session.get(User.class, id);
            tx.commit();
        } catch (Exception e) {
            System.err.println("Ошибка поиска пользователя");
            log.error("Failed to find user with id {}", id);
            throw new RuntimeException();
        }
        return user;
    }

    @Override
    public void save(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            user.setCreatedAt(LocalDateTime.now());
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            log.info("User saved: {}", user);
        } catch (HibernateException e) {
            rollbackIsActive(tx);
            if (e instanceof SQLGrammarException) {
                System.err.println("Ошибка синтаксиса SQL: " + e.getMessage());
            } else System.err.println("Ошибка Hibernate");
            throw new RuntimeException();
        } catch (PersistenceException e) {
            rollbackIsActive(tx);
            if (e.getCause() instanceof ConstraintViolationException) {
                System.err.println("Ошибка ограничений базы данных: " + e.getCause().getCause().getMessage());
            } else System.err.println("Ошибка при работе с JPA");
            throw new RuntimeException();
        } catch (Exception e) {
            rollbackIsActive(tx);
            System.err.println("Неизвестная ошибка");
            log.error("Unexpected error");
            throw new RuntimeException();
        } finally {
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
        } catch (HibernateException e) {
            rollbackIsActive(tx);
            if (e instanceof SQLGrammarException) {
                System.err.println("Ошибка синтаксиса SQL: " + e.getMessage());
            } else System.err.println("Ошибка Hibernate");
            throw new RuntimeException();
        } catch (PersistenceException e) {
            rollbackIsActive(tx);
            if (e.getCause() instanceof ConstraintViolationException) {
                System.err.println("Ошибка ограничений базы данных: " + e.getCause().getCause().getMessage());
            } else System.err.println("Ошибка при работе с JPA");
            throw new RuntimeException();
        } catch (Exception e) {
            rollbackIsActive(tx);
            System.err.println("Неизвестная ошибка");
            log.error("Unexpected error on update: {}", e.getMessage());
            throw new RuntimeException();
        } finally {
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
        } catch (HibernateException e) {
            rollbackIsActive(tx);
            if (e instanceof SQLGrammarException) {
                System.err.println("Ошибка синтаксиса SQL: " + e.getMessage());
            } else System.err.println("Ошибка Hibernate");
            throw new RuntimeException();
        } catch (PersistenceException e) {
            rollbackIsActive(tx);
            if (e.getCause() instanceof ConstraintViolationException) {
                System.err.println("Ошибка ограничений базы данных: " + e.getCause().getCause().getMessage());
            } else System.err.println("Ошибка при работе с JPA");
            throw new RuntimeException();
        } catch (Exception e) {
            rollbackIsActive(tx);
            System.err.println("Неизвестная ошибка");
            log.error("Unexpected error on delete: {}", e.getMessage());
            throw new RuntimeException();
        } finally {
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
}
