package org.example;

import org.example.config.HibernateUtils;
import org.example.entity.User;
import org.example.repository.impl.UserDAOImpl;
import org.hibernate.Session;

public class HibernateApplication {
    public static void main(String[] args) {
        Session session = HibernateUtils.getSessionFactory().openSession();

        UserDAOImpl rep = new UserDAOImpl(session);

        User user = new User();
        user.setName("Jack");
        user.setAge(22);
        user.setEmail("example@gmail.com");

        rep.save(user);
        System.out.println(rep.findAll());

        user.setAge(30);
        rep.update(user);
        System.out.println(rep.findAll());

        rep.delete(user.getId());
        System.out.println(rep.findAll());
        session.close();
    }
}