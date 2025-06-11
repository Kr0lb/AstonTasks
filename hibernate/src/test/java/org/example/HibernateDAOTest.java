package org.example;

import jakarta.persistence.PersistenceException;
import org.example.entity.User;
import org.example.repository.impl.UserDAOImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HibernateDAOTest {
    private PostgreSQLContainer<?> postgresContainer;
    private SessionFactory sessionFactory;
    private UserDAOImpl userDAO;

    @BeforeAll
    void setup() {
        postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        postgresContainer.start();

        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        cfg.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        cfg.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        cfg.setProperty("hibernate.connection.password", postgresContainer.getPassword());
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        cfg.setProperty("hibernate.show_sql", "true");
        cfg.addAnnotatedClass(User.class);

        sessionFactory = cfg.buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
    }

    @AfterAll
    void shutDown() {
        if (sessionFactory != null) sessionFactory.close();
        if (postgresContainer != null) postgresContainer.stop();
    }

    @Test
    void testSave() {
        User user = new User();
        user.setName("Test User");
        userDAO.save(user);

        Assertions.assertNotNull(user.getId());

        User found = userDAO.findById(user.getId());
        Assertions.assertEquals("Test User", found.getName());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setName("Test User");
        userDAO.save(user);

        user.setName("Updated Test User");
        userDAO.update(user);

        Assertions.assertNotNull(user.getId());

        User found = userDAO.findById(user.getId());
        Assertions.assertEquals("Updated Test User", found.getName());
    }

    @Test
    void testFindAll() {
        userDAO.save(new User("test user 1"));
        userDAO.save(new User("test user 2"));

        List<User> users = userDAO.findAll();
        Assertions.assertTrue(users.size() >= 2);
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setName("DeleteMe");
        userDAO.save(user);

        Long id = user.getId();
        userDAO.delete(id);

        Assertions.assertNull(userDAO.findById(id));
    }

    @Test
    void testRollback() {
        User user = new User();
        user.setName("Test User");
        userDAO.save(user);

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Test User");
        assertThrows(RuntimeException.class, () -> userDAO.save(user1));

        Assertions.assertNotNull(user.getId());
    }

    @Test
    void testSave_throwsSQLGrammarException() {
        User user = new User();
        Session session = Mockito.mock(Session.class);
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Transaction tx = Mockito.mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        doThrow(new SQLGrammarException("неверный SQL", null)).when(session).persist(user);

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.save(user));
    }

    @Test
    void testSave_throwsConstraintViolationException() {
        User user = new User();
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        Transaction tx = mock(Transaction.class);

        ConstraintViolationException violation = new ConstraintViolationException("Уникальный индекс", null, "user_id_unique");
        PersistenceException pe = new PersistenceException(violation);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        doThrow(pe).when(session).persist(user);

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.save(user));
    }

    @Test
    void testSave_throwsException() {
        User user = new User();
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        Transaction tx = mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        doThrow(new RuntimeException("другие ошибки", null)).when(session).persist(user);

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.save(user));
    }


    @Test
    void testUpdate_throwsSQLGrammarException() {
        User user = new User();
        Session session = Mockito.mock(Session.class);
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Transaction tx = Mockito.mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        doThrow(new SQLGrammarException("неверный SQL", null)).when(session).merge(user);

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.update(user));
    }

    @Test
    void testUpdate_throwsConstraintViolationException() {
        User user = new User();
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        Transaction tx = mock(Transaction.class);

        ConstraintViolationException violation = new ConstraintViolationException("Уникальный индекс", null, "user_id_unique");
        PersistenceException pe = new PersistenceException(violation);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        doThrow(pe).when(session).merge(user);

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.update(user));
    }

    @Test
    void testUpdate_throwsException() {
        User user = new User();
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        Transaction tx = mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        doThrow(new RuntimeException("другие ошибки", null)).when(session).merge(user);

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.update(user));
    }

    @Test
    void testDelete_throwsSQLGrammarException() {
        Long id = 1L;
        Session session = Mockito.mock(Session.class);
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        Transaction tx = Mockito.mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        when(session.get(User.class, id)).thenThrow(new SQLGrammarException("неверный SQL", null));

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.delete(id));
    }

    @Test
    void testDelete_throwsConstraintViolationException() {
        Long id = 1L;
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        Transaction tx = mock(Transaction.class);

        ConstraintViolationException violation = new ConstraintViolationException("Уникальный индекс", null, "user_id_unique");
        PersistenceException pe = new PersistenceException(violation);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        when(session.get(User.class, id)).thenThrow(new PersistenceException(violation));

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.delete(id));
    }

    @Test
    void testDelete_throwsException() {
        Long id = 1L;
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        Transaction tx = mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        when(session.get(User.class, id)).thenThrow(new RuntimeException("другие ошибки", null));

        UserDAOImpl userDao = new UserDAOImpl(sessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.delete(id));
    }
}
