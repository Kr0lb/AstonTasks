package org.example;

import org.example.entity.User;
import org.example.repository.impl.UserDAOImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

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
}
