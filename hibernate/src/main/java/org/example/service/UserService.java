package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserDAO;

@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public User getUSerById(Long id) {
        return userDAO.findById(id);
    }

    public void createUser(User user) {
        userDAO.save(user);
    }
}
