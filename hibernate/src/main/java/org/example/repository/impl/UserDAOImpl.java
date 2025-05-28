package org.example.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.repository.UserDAO;
import org.hibernate.Session;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final Session session;

    @Override
    public User findById(Long id) {
        return this.session.get(User.class, id);
    }

    @Override
    public void save(User user) {
        this.session.beginTransaction();
        this.session.persist(user);
        this.session.getTransaction().commit();
        log.info("User saved: {}", user.toString());
    }

    @Override
    public void update(User user) {
        this.session.merge(user);
        log.info("User update: {}", user.toString());
    }

    @Override
    public void delete(Long id) {
        this.session.beginTransaction();
        this.session.remove(this.findById(id));
        this.session.getTransaction().commit();
        log.info("User with id {} was deleted", id);
    }

    @Override
    public List<User> findAll() {
        return this.session.createQuery("from User", User.class).getResultList();
    }
}
