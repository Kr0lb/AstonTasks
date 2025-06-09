package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserDAO;

@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public User getUserById(Long id) {
        User user = userDAO.findById(id);
        if (user == null)
            throw new RuntimeException("User not found");
        return user;
    }

    public void createUser(User user) {
        userDAO.save(user);
    }
}
