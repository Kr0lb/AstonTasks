package org.example.repository;

import org.example.entity.User;

import java.util.List;

public interface UserDAO {
    User findById(Long id);

    void save(User user);

    void update(User user);

    void delete(Long id);

    List<User> findAll();
}
