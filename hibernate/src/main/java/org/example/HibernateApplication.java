package org.example;

import org.example.entity.User;
import org.example.repository.impl.UserRepository;

public class HibernateApplication {
    public static void main(String[] args) {
        UserRepository ur = new UserRepository();
        System.out.println(ur.findById(1L));

        System.out.println(ur.findAll());
    }
}